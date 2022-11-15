package com.sesame.game.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.sesame.game.common.Const;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.action.SudokuPanelMouseAdapter;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * square panel
 */
public class SquarePanel extends JPanel {

    private final Font f = new Font("Times New Roman", Font.PLAIN, Const.NORMAL_FONT_SIZE);
    public SudokuPuzzle puzzle;
    public int currentlySelectedCol;
    public int currentlySelectedRow;
    public int usedWidth;
    public int usedHeight;
    /**
     * hind mode
     */
    public boolean isHintMode;
    /**
     * hint data model
     */
    public HintModel hintModel;
    /**
     * node mode
     */
    public boolean isNoteMode;
    /**
     * has user noted
     */
    public boolean isUserNoted;
    public boolean showCandidate;
    private int slotWidth;
    private int slotHeight;

    public SquarePanel() {
        this.setPreferredSize(new Dimension(540, 450));
        this.addMouseListener(new SudokuPanelMouseAdapter(this));
    }

    public void newSudokuPuzzle(SudokuPuzzle puzzle, boolean showCandidate) {
        this.puzzle = puzzle;
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        usedWidth = 0;
        usedHeight = 0;
        isHintMode = false;
        hintModel = null;
        isNoteMode = false;
        isUserNoted = false;
        this.showCandidate = showCandidate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        slotWidth = this.getWidth() / Const.ROWS;
        slotHeight = this.getHeight() / Const.ROWS;
        usedWidth = (this.getWidth() / Const.COLUMNS) * Const.COLUMNS;
        usedHeight = (this.getHeight() / Const.ROWS) * Const.ROWS;

        g2d.setColor(new Color(1.0f, 1.0f, 1.0f));
        g2d.fillRect(0, 0, usedWidth, usedHeight);

        g2d.setColor(Color.DARK_GRAY);

        for (int x = 0; x <= usedWidth; x += slotWidth) {
            if ((x / slotWidth) % Const.BOX_WIDTH == 0) {
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(x, 0, x, usedHeight);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(x, 0, x, usedHeight);
            }
        }
        ////this will draw the right most line
        //g2d.drawLine(usedWidth - 1, 0, usedWidth - 1, usedHeight);
        //g2d.drawLine(1, 0, 1, usedHeight);

        for (int y = 0; y <= usedHeight; y += slotHeight) {
            if ((y / slotHeight) % Const.BOX_HEIGHT == 0) {
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(0, y, usedWidth, y);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(0, y, usedWidth, y);
            }
        }
        ////this will draw the bottom line
        //g2d.drawLine(0, usedHeight - 1, usedWidth, usedHeight - 1);
        //g2d.drawLine(0, 1, usedWidth, 1);
        g2d.setFont(f);
        FontRenderContext fContext = g2d.getFontRenderContext();

        drawDigital(g2d, slotWidth, slotHeight, fContext);
        if (showCandidate) {
            drawCandidate(g2d, slotWidth, slotHeight, fContext);
        }
        // draw selected cell
        if (!isHintMode) {
            if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
                g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
                g2d.fillRect(currentlySelectedCol * slotWidth, currentlySelectedRow * slotHeight, slotWidth,
                    slotHeight);
            }
            return;
        }

        drawHint(g2d, slotWidth, slotHeight, f, fContext);

    }

    private void drawDigital(Graphics2D g2d, int slotWidth, int slotHeight, FontRenderContext fContext) {
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                if (!puzzle.isSlotAvailable(row, col)) {
                    int textWidth = (int)f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
                    int textHeight = (int)f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();

                    if (puzzle.isSlotMutable(row, col)) {
                        g2d.setColor(new Color(0.25390625f, 0.41015625f, 0.87890625f));
                    } else {
                        g2d.setColor(Color.BLACK);
                    }
                    g2d.drawString(puzzle.getValue(row, col), (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
                        (row * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
                }
            }
        }
    }

    private void drawCandidate(Graphics2D g2d, int slotWidth, int slotHeight, FontRenderContext fContext) {
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

                    // hint mode and candidate mode
                    if (isHintMode && hintModel.isCandidateModel()) {
                        CandidateModel candidateModel = hintModel.getCandidateModel();
                        Position onePosition = new Position(row, col);
                        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();

                        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                        if (causeMap.containsKey(onePosition) && causeMap.get(onePosition).contains(digital)) {
                            g2d.setColor(Color.RED);
                        } else if ((deleteMap.containsKey(onePosition)) && (!CollectionUtils.isEmpty(
                            deleteMap.get(onePosition)))
                            && deleteMap.get(onePosition).contains(digital)) {
                            g2d.setColor(Color.GREEN);
                        } else {
                            g2d.setColor(Color.gray);
                        }
                    } else {
                        g2d.setColor(Color.gray);
                    }
                    g2d.drawString(digital, (col * slotWidth) + (xStart + (slotWidth / 6) - (candidateTextWidth / 2)),
                        (row * slotHeight) + (yStart + (candidateTextHeight / 2)));
                }
            }
        }
    }

    private void drawHint(Graphics2D g2d, int slotWidth, int slotHeight, Font f, FontRenderContext fContext) {
        Assert.notNull(hintModel, "hintModel should not be null");

        if (!hintModel.isCandidateModel()) {
            Font smallFont = new Font("Times New Roman", Font.PLAIN, Const.NORMAL_FONT_SIZE);
            g2d.setFont(smallFont);

            //hint digital
            SolutionModel solutionModel = hintModel.getSolutionModel();
            Position position = solutionModel.getPosition();
            String found = solutionModel.getSolutionDigital();
            int textWidth = (int)f.getStringBounds(found, fContext).getWidth();
            int textHeight = (int)f.getStringBounds(found, fContext).getHeight();
            g2d.setColor(Color.MAGENTA);
            g2d.drawString(found, (position.getCol() * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
                (position.getRow() * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
            //hint background
            g2d.setColor(new Color(0.117f, 0.562f, 1.0f, 0.6f));

            g2d.fillRect(position.getCol() * slotWidth, position.getRow() * slotHeight, slotWidth,
                slotHeight);
            //related cells
            g2d.setColor(new Color(0.527f, 0.804f, 0.976f, 0.6f));
            solutionModel.getRelated().stream().forEach(
                one -> g2d.fillRect(one.getCol() * slotWidth, one.getRow() * slotHeight, slotWidth,
                    slotHeight));
        } else {
            //related cells
            g2d.setColor(new Color(0.527f, 0.804f, 0.976f, 0.45f));

            CandidateModel candidateModel = hintModel.getCandidateModel();
            candidateModel.getCauseMap().keySet().stream().forEach(
                one -> g2d.fillRect(one.getCol() * slotWidth, one.getRow() * slotHeight, slotWidth,
                    slotHeight));
        }

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        if (CollectionUtils.isEmpty(unitModelList)) {
            return;
        }

        unitModelList.stream().forEach(

            one -> {
                if (one.getUnit() == Unit.ROW) {
                    drawRowLine(one.getRow(), g2d);
                } else if (one.getUnit() == Unit.COLUMN) {
                    drawColumnLine(one.getColumn(), g2d);
                } else if (one.getUnit() == Unit.BOX) {
                    drawBoxLine(one.getRow(), one.getColumn(), g2d);
                } else {
                    throw new RuntimeException("should be here");
                }
            }

        );
    }

    public void drawRowLine(int row, Graphics2D g2d) {
        g2d.setColor(new Color(0.0f, 0.804f, 0.816f));
        g2d.setStroke(new BasicStroke(2));

        g2d.drawLine(0, row * slotHeight, Const.ROWS * slotWidth, row * slotHeight);
        g2d.drawLine(0, (row + 1) * slotHeight, Const.ROWS * slotWidth, (row + 1) * slotHeight);

        g2d.drawLine(0, row * slotHeight, 0, (row + 1) * slotHeight);
        g2d.drawLine(Const.ROWS * slotWidth, row * slotHeight, Const.ROWS * slotWidth, (row + 1) * slotHeight);
    }

    public void drawColumnLine(int column, Graphics2D g2d) {
        g2d.setColor(new Color(0.0f, 0.804f, 0.816f));
        g2d.setStroke(new BasicStroke(2));

        g2d.drawLine(column * slotWidth, 0, (column + 1) * slotWidth, 0);
        g2d.drawLine((column + 1) * slotWidth, 0, (column + 1) * slotWidth, Const.COLUMNS * slotHeight);

        g2d.drawLine(column * slotWidth, 0, column * slotWidth, Const.COLUMNS * slotHeight);
        g2d.drawLine((column + 1) * slotWidth, 0, (column + 1) * slotWidth, Const.COLUMNS * slotHeight);
    }

    public void drawBoxLine(int inputRow, int inputColumn, Graphics2D g2d) {
        g2d.setColor(new Color(0.0f, 0.804f, 0.816f));
        g2d.setStroke(new BasicStroke(2));

        int row = inputRow - inputRow % Const.BOX_WIDTH;
        int column = inputColumn - inputColumn % Const.BOX_WIDTH;

        int x = column * slotWidth;
        int y = row * slotHeight;

        g2d.drawLine(x, y, x + 3 * slotWidth, y);
        g2d.drawLine(x, y + 3 * slotHeight, x + 3 * slotWidth, y + 3 * slotHeight);
        g2d.drawLine(x, y, x, y + 3 * slotHeight);
        g2d.drawLine(x + 3 * slotWidth, y, x + 3 * slotWidth, y + 3 * slotHeight);

    }

}
