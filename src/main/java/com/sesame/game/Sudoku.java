package com.sesame.game;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.tool.SudokuGenerator;
import com.sesame.game.ui.ButtonPanel;
import com.sesame.game.ui.MenuGenerator;
import com.sesame.game.ui.SquarePanel;
import lombok.Getter;

/**
 * the Sudoku main entry
 *
 * @author sesame
 */
public class Sudoku extends JFrame {

    private final SquarePanel squarePanel;
    @Getter
    private ButtonPanel buttonPanel;
    @Getter
    private MenuGenerator menuGenerator;

    public Sudoku() {
        //jFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(I18nProcessor.getValue("sudoku"));
        this.setMinimumSize(new Dimension(800, 600));
        //squarePanel
        squarePanel = new SquarePanel();
        //buttonPanel
        buttonPanel = new ButtonPanel(squarePanel, this);
        //menu
        menuGenerator = new MenuGenerator(squarePanel, this);
        JMenuBar menuBar = menuGenerator.buildJMenuBar();
        this.setJMenuBar(menuBar);
        //windowPanel
        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800, 600));
        windowPanel.add(squarePanel);
        windowPanel.add(buttonPanel);
        this.add(windowPanel);
        //load default game
        loadLevelGameRebuild(GameLevel.EASY, 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Sudoku frame = new Sudoku();
            frame.setVisible(true);
        });
    }

    public void loadLevelGameRebuild(GameLevel gameLevel, int caseNumber) {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.useLevelGame(gameLevel, caseNumber);
        if (gameLevel == GameLevel.EASY) {
            rebuildInterface(generatedPuzzle, false);
        } else {
            rebuildInterface(generatedPuzzle, true);
        }
    }

    public void rebuildInterface(SudokuPuzzle generatedPuzzle, boolean showCandidate) {
        squarePanel.newSudokuPuzzle(generatedPuzzle, showCandidate);
        squarePanel.repaint();
        buttonPanel.buttonModel();
    }

}
