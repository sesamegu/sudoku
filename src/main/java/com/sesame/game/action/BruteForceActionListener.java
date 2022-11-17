package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.sesame.game.Sudoku;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.tool.BruteForceSolver;
import com.sesame.game.ui.SquarePanel;

/**
 * Introduction:Brute Force Action Listener
 *
 * @author sesame 2022/11/16
 */
public class BruteForceActionListener implements ActionListener {
    private final Sudoku sudoku;

    public BruteForceActionListener(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //set the waiting text and disable the button
        sudoku.getButtonPanel().setUnAvailableLabel(I18nProcessor.getValue("brute_force_start"));
        JButton source = (JButton)e.getSource();
        source.setEnabled(false);
        //start a computing thread
        new Thread(new ComputingThread(sudoku, source)).start();
    }
}

class ComputingThread implements Runnable {

    private final Sudoku sudoku;
    private final JButton jButton;

    ComputingThread(Sudoku sudoku, JButton button) {
        this.sudoku = sudoku;
        this.jButton = button;
    }

    @Override
    public void run() {
        SquarePanel squarePanel = sudoku.getSquarePanel();
        SudokuPuzzle puzzle = squarePanel.puzzle;
        boolean resolved = BruteForceSolver.backtrackSudokuSolver(0, 0, puzzle);
        puzzle.resetCandidate();
        if (resolved) {
            sudoku.getButtonPanel().setUnAvailableLabel(I18nProcessor.getValue("puzzle_finished"));
        } else {
            sudoku.getButtonPanel().setUnAvailableLabel(I18nProcessor.getValue("not_solvable"));
        }
        jButton.setEnabled(true);

        squarePanel.repaint();

        // start the hide thread
        new Thread(new HideTheTextThread(sudoku)).start();

    }
}
