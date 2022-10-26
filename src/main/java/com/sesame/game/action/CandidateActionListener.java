package com.sesame.game.action;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sesame.game.SudokuPanel;

/**
 * Introduction:
 *
 * @author sesame 2022/10/26
 */
public class CandidateActionListener implements ChangeListener {
    private SudokuPanel sudokuPanel;
    private JToggleButton candidateButton;

    public CandidateActionListener(SudokuPanel sudokuPanel, JToggleButton candidateButton) {
        this.sudokuPanel = sudokuPanel;
        this.candidateButton = candidateButton;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (candidateButton.isSelected()) {
            sudokuPanel.setNoteMode(true);
            candidateButton.setText("Note ON");
            candidateButton.repaint();
        } else {
            sudokuPanel.setNoteMode(false);
            candidateButton.setText("Note Off");
        }
    }
}