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
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import com.sesame.game.action.HideTheTextThread;
import com.sesame.game.strategy.FillStrategy;
import com.sesame.game.strategy.HiddenPairsStrategy;
import com.sesame.game.strategy.HiddenSinglesStrategy;
import com.sesame.game.strategy.HiddenThreeStrategy;
import com.sesame.game.strategy.HiddenTriplesStrategy;
import com.sesame.game.strategy.LastFreeCellStrategy;
import com.sesame.game.strategy.LastPossibleNumberStrategy;
import com.sesame.game.strategy.ObviousPairsStrategy;
import com.sesame.game.strategy.ObviousTriplesStrategy;
import com.sesame.game.strategy.PointingStrategy;
import com.sesame.game.strategy.Position;
import com.sesame.game.strategy.SwordFishStrategy;
import com.sesame.game.strategy.XWingStrategy;
import com.sesame.game.strategy.YWingStrategy;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.SolutionModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("serial")
public class SudokuPanel extends JPanel {

    @Getter
    private SudokuPuzzle puzzle;
    private int currentlySelectedCol;
    private int currentlySelectedRow;
    private int usedWidth;
    private int usedHeight;
    private boolean isHintMode;
    private HintModel hintModel;

    /**
     * 是否笔记模式
     */
    @Getter
    @Setter
    private boolean isNoteMode;

    /**
     * 用户是否做过标记，如果做过标记，那么在Hint时，需要重置标志，因为用户的标志不可信
     */
    private boolean isUserNoted;

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
        isNoteMode = false;
        isUserNoted = false;
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
                        Position onePosition = new Position(row, col);
                        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();

                        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                        if (causeMap.containsKey(onePosition) && causeMap.get(onePosition).contains(digital)) {
                            g2d.setColor(Color.RED);
                        } else if ((deleteMap.containsKey(onePosition)) && (!CollectionUtils.isEmpty(
                            deleteMap.get(onePosition)))
                            && deleteMap.get(onePosition).contains(digital)) {
                            g2d.setColor(Color.BLUE);
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
            candidateModel.getCauseMap().keySet().stream().forEach(
                one -> g2d.fillRect(one.getCol() * slotWidth, one.getRow() * slotHeight, slotWidth,
                    slotHeight));
        }

    }

    public Optional<HintModel> tryAllStrategy() {
        List<FillStrategy> allStrategy = new ArrayList<>();
        allStrategy.add(new LastFreeCellStrategy());
        allStrategy.add(new LastPossibleNumberStrategy());
        allStrategy.add(new HiddenSinglesStrategy());
        allStrategy.add(new ObviousPairsStrategy());
        allStrategy.add(new ObviousTriplesStrategy());
        allStrategy.add(new HiddenPairsStrategy());
        allStrategy.add(new HiddenTriplesStrategy());
        allStrategy.add(new HiddenThreeStrategy());
        allStrategy.add(new PointingStrategy());
        allStrategy.add(new XWingStrategy());
        allStrategy.add(new YWingStrategy());
        allStrategy.add(new SwordFishStrategy());

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
            if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
                String digital = ((JButton)e.getSource()).getText();

                if (isNoteMode) {
                    //已填充数字，则无响应
                    if (puzzle.isSlotValid(currentlySelectedRow, currentlySelectedCol)) {
                        return;
                    }

                    List<String> candidate = puzzle.getCandidate(currentlySelectedRow, currentlySelectedCol);
                    //为空直接添加
                    if (CollectionUtils.isEmpty(candidate)) {
                        List<String> canList = new ArrayList<>();
                        canList.add(digital);
                        puzzle.setCandidate(currentlySelectedRow, currentlySelectedCol, canList);
                    } else {
                        //取反
                        if (candidate.contains(digital)) {
                            candidate.remove(digital);
                        } else {
                            candidate.add(digital);
                        }
                    }
                    isUserNoted = true;
                } else {
                    puzzle.makeMove(currentlySelectedRow, currentlySelectedCol, digital,
                        true);
                }

                repaint();
            }
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
            //如果用户做过标志，需要重置候选数
            if (isUserNoted) {
                puzzle.resetCandidate();
                isUserNoted = false;
            }

            // 执行策略
            Optional<HintModel> result = tryAllStrategy();
            if (result.isPresent()) {
                isHintMode = true;
                hintModel = result.get();
                sudokuFrame.hintModel(hintModel.getStrategy());

                // 命中解决方案后，直接删除候选数
                if (!hintModel.isCandidate()) {
                    SolutionModel solutionModel = hintModel.getSolutionModel();
                    Position position = solutionModel.getPosition();
                    puzzle.deleteCandidate(position.getRow(), position.getCol(),
                        puzzle.getCandidate(position.getRow(), position.getCol()));

                }
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
            //如果用户做过标志，需要重置候选数
            if (isUserNoted) {
                puzzle.resetCandidate();
                isUserNoted = false;
            }

            // 执行策略
            Optional<HintModel> result = tryAllStrategy();
            while (result.isPresent()) {
                HintModel hm = result.get();
                if (hm.isCandidate()) {
                    CandidateModel candidateModel = hm.getCandidateModel();
                    Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                    deleteMap.entrySet().forEach(
                        one -> puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
                    );
                } else {
                    SolutionModel solutionModel = hm.getSolutionModel();
                    puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                        solutionModel.getValue(), true);
                }
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
                Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                deleteMap.entrySet().forEach(
                    one -> puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
                );
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

}
