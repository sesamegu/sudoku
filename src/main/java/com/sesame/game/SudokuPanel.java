package com.sesame.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import com.sesame.game.strategy.FillStrategy;
import com.sesame.game.strategy.HiddenSinglesStrategy;
import com.sesame.game.strategy.LastFreeCellStrategy;
import com.sesame.game.strategy.LastPossibleNumberStrategy;
import com.sesame.game.strategy.ObviousPairsStrategy;
import com.sesame.game.strategy.ObviousTriplesStrategy;
import com.sesame.game.strategy.Position;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.SolutionModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("serial")
public class SudokuPanel extends JPanel {

    private SudokuPuzzle puzzle;
    private int currentlySelectedCol;
    private int currentlySelectedRow;
    private int usedWidth;
    private int usedHeight;
    private boolean isHintMode;
    private HintModel hintModel;

    public SudokuPanel() {
        this.setPreferredSize(new Dimension(540, 450));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.puzzle = new SudokuGenerator().generateRandomSudoku(SudokuPuzzleType.NORMAL);
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        usedWidth = 0;
        usedHeight = 0;
        isHintMode = false;
        hintModel = null;
    }

    public void newSudokuPuzzle(SudokuPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(new Color(1.0f, 1.0f, 1.0f));

        int slotWidth = this.getWidth() / Const.ROWS;
        int slotHeight = this.getHeight() / Const.ROWS;

        usedWidth = (this.getWidth() / puzzle.getNumColumns()) * puzzle.getNumColumns();
        usedHeight = (this.getHeight() / Const.ROWS) * Const.ROWS;

        g2d.fillRect(0, 0, usedWidth, usedHeight);

        g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
        for (int x = 0; x <= usedWidth; x += slotWidth) {
            if ((x / slotWidth) % puzzle.getBoxWidth() == 0) {
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, 0, x, usedHeight);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(x, 0, x, usedHeight);
            }
        }
        //this will draw the right most line
        //g2d.drawLine(usedWidth - 1, 0, usedWidth - 1,usedHeight);
        for (int y = 0; y <= usedHeight; y += slotHeight) {
            if ((y / slotHeight) % puzzle.getBoxHeight() == 0) {
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, y, usedWidth, y);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(0, y, usedWidth, y);
            }
        }
        //this will draw the bottom line
        //g2d.drawLine(0, usedHeight - 1, usedWidth, usedHeight - 1);

        Font f = new Font("Times New Roman", Font.PLAIN, Const.NORMAL_FONT_SIZE);
        g2d.setFont(f);
        FontRenderContext fContext = g2d.getFontRenderContext();
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < puzzle.getNumColumns(); col++) {
                if (!puzzle.isSlotAvailable(row, col)) {
                    int textWidth = (int)f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
                    int textHeight = (int)f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();

                    // 区分颜色
                    if (puzzle.isSlotMutable(row, col)) {
                        g2d.setColor(Color.GRAY);
                    } else {
                        g2d.setColor(Color.BLACK);
                    }
                    g2d.drawString(puzzle.getValue(row, col), (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
                        (row * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
                }
            }
        }

        //候选数值
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                List<String> candidate = puzzle.getCandidate(row, col);
                if (CollectionUtils.isEmpty(candidate)) {
                    continue;
                }
                Assert.isTrue(!puzzle.isSlotValid(row, col), "Must valid");
                Font smallFont = new Font("Times New Roman", Font.PLAIN, Const.HINT_FONT_SIZE);
                g2d.setFont(smallFont);

                for (String digital : candidate) {
                    int candidateTextWidth = (int)f.getStringBounds(digital, fContext).getWidth();
                    int candidateTextHeight = (int)f.getStringBounds(digital, fContext).getHeight();

                    int number = Integer.parseInt(digital);
                    int xStart = (number - 1) % 3 * (slotWidth / 3);
                    int yStart = (number - 1) / 3 * (slotHeight / 3);

                    //提示模式 且 是候选数模式
                    if (isHintMode && hintModel.isCandidate()) {
                        CandidateModel candidateModel = hintModel.getCandidateModel();
                        Position o = new Position(row, col);
                        List<Position> causeList = candidateModel.getCauseList();
                        List<Position> relatedList = candidateModel.getRelatedList();
                        List<String> digitalString = candidateModel.getDigitalString();
                        if ((causeList.contains(o) || relatedList.contains(o)) && digitalString.contains(digital)) {
                            g2d.setColor(Color.red);
                        } else {
                            g2d.setColor(Color.LIGHT_GRAY);
                        }
                    } else {
                        g2d.setColor(Color.LIGHT_GRAY);
                    }
                    g2d.drawString(digital, (col * slotWidth) + (xStart + (slotWidth / 6) - (candidateTextWidth / 2)),
                        (row * slotHeight) + (yStart + (candidateTextHeight / 2)));
                }
            }
        }

        if (!isHintMode) {
            if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
                g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
                g2d.fillRect(currentlySelectedCol * slotWidth, currentlySelectedRow * slotHeight, slotWidth,
                    slotHeight);
            }
            return;
        }

        // 提示模式下
        Assert.notNull(hintModel, "hintModel should not be null");

        if (!hintModel.isCandidate()) {
            Font smallFont = new Font("Times New Roman", Font.PLAIN, Const.NORMAL_FONT_SIZE);
            g2d.setFont(smallFont);

            //提示数值
            SolutionModel solutionModel = hintModel.getSolutionModel();
            Position position = solutionModel.getPosition();
            String found = solutionModel.getValue();
            int textWidth = (int)f.getStringBounds(found, fContext).getWidth();
            int textHeight = (int)f.getStringBounds(found, fContext).getHeight();
            g2d.setColor(Color.PINK);
            g2d.drawString(found, (position.getCol() * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
                (position.getRow() * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
            //提示背景
            g2d.setColor(new Color(0.0f, 0.0f, 0.9f, 0.3f));
            g2d.fillRect(position.getCol() * slotWidth, position.getRow() * slotHeight, slotWidth,
                slotHeight);
            //相关单元格
            g2d.setColor(new Color(0.0f, 0.0f, 0.5f, 0.3f));
            solutionModel.getRelated().stream().forEach(
                one -> g2d.fillRect(one.getCol() * slotWidth, one.getRow() * slotHeight, slotWidth,
                    slotHeight));
        } else {
            //相关单元格
            g2d.setColor(new Color(0.0f, 0.0f, 0.5f, 0.3f));
            CandidateModel candidateModel = hintModel.getCandidateModel();
            candidateModel.getCauseList().stream().forEach(
                one -> g2d.fillRect(one.getCol() * slotWidth, one.getRow() * slotHeight, slotWidth,
                    slotHeight));
        }

    }

    public void messageFromNumActionListener(String buttonValue) {
        if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
            puzzle.makeMove(currentlySelectedRow, currentlySelectedCol, buttonValue, true);
            repaint();
        }
    }

    public Optional<HintModel> tryAllStrategy() {
        List<FillStrategy> allStrategy = new ArrayList<>();
        allStrategy.add(new LastFreeCellStrategy());
        allStrategy.add(new LastPossibleNumberStrategy());
        allStrategy.add(new HiddenSinglesStrategy());
        allStrategy.add(new ObviousPairsStrategy());
        allStrategy.add(new ObviousTriplesStrategy());

        for (FillStrategy one : allStrategy) {
            Optional<HintModel> hintModel = one.tryStrategy(puzzle);
            if (hintModel.isPresent()) {
                return hintModel;
            }
        }

        return Optional.empty();
    }

    public class NumActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            messageFromNumActionListener(((JButton)e.getSource()).getText());
        }
    }

    public class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
                if (puzzle.isSlotMutable(currentlySelectedRow, currentlySelectedCol) && puzzle.isSlotValid(
                    currentlySelectedRow, currentlySelectedCol)) {
                    puzzle.makeSlotEmpty(currentlySelectedRow, currentlySelectedCol);
                }
                repaint();
            }
        }
    }

    public class HintActionListener implements ActionListener {
        private final SudokuFrame sudokuFrame;

        public HintActionListener(SudokuFrame sudokuFrame) {
            this.sudokuFrame = sudokuFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Optional<HintModel> result = tryAllStrategy();
            if (result.isPresent()) {
                isHintMode = true;
                hintModel = result.get();
                sudokuFrame.hintModel(hintModel.getStrategy());
                repaint();
            } else {
                sudokuFrame.setUnAvailableLabel("无可用技巧");
                new Thread(new HideTheTextThread(sudokuFrame)).start();
            }
        }

    }

    public class UnStopHintListener implements ActionListener {
        private final SudokuFrame sudokuFrame;

        public UnStopHintListener(SudokuFrame sudokuFrame) {
            this.sudokuFrame = sudokuFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Optional<HintModel> result = tryAllStrategy();
            while (result.isPresent()) {
                HintModel hm = result.get();
                if (hm.isCandidate()) {
                    // todo 候选者策略的处理
                    break;
                }

                SolutionModel solutionModel = hm.getSolutionModel();
                puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                    solutionModel.getValue(), true);
                result = tryAllStrategy();

            }
            repaint();

            sudokuFrame.setUnAvailableLabel("已完成探索");
            new Thread(new HideTheTextThread(sudokuFrame)).start();

        }
    }

    public class ApplyListener implements ActionListener {
        private final SudokuFrame sudokuFrame;

        public ApplyListener(SudokuFrame sudokuFrame) {
            this.sudokuFrame = sudokuFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (hintModel.isCandidate()) {
                CandidateModel candidateModel = hintModel.getCandidateModel();
                List<Position> relatedList = candidateModel.getRelatedList();
                relatedList.forEach(
                    one -> puzzle.deleteCandidate(one.getRow(), one.getCol(), candidateModel.getDigitalString()));
            } else {
                SolutionModel solutionModel = hintModel.getSolutionModel();
                puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                    solutionModel.getValue(),
                    true);
                currentlySelectedRow = solutionModel.getPosition().getRow();
                currentlySelectedCol = solutionModel.getPosition().getCol();
            }

            isHintMode = false;
            hintModel = null;
            sudokuFrame.buttonModel();
            repaint();
        }
    }

    private class SudokuPanelMouseAdapter extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int slotWidth = usedWidth / puzzle.getNumColumns();
                int slotHeight = usedHeight / Const.ROWS;
                currentlySelectedRow = e.getY() / slotHeight;
                currentlySelectedCol = e.getX() / slotWidth;
                e.getComponent().repaint();
            }
        }
    }

    private class HideTheTextThread implements Runnable {
        private final SudokuFrame sudokuFrame;

        private HideTheTextThread(SudokuFrame sudokuFrame) {this.sudokuFrame = sudokuFrame;}

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
            }
            sudokuFrame.setUnAvailableLabel("");
        }
    }
}
