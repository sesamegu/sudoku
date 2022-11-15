package com.sesame.game.action;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sesame.game.ui.SquarePanel;
import com.sesame.game.i18n.I18nProcessor;

/**
 * Introduction: candidate model switch
 *
 * @author sesame 2022/10/26
 */
public class CandidateActionListener implements ChangeListener {
    private final SquarePanel squarePanel;
    private final JToggleButton candidateButton;

    public CandidateActionListener(SquarePanel squarePanel, JToggleButton candidateButton) {
        this.squarePanel = squarePanel;
        this.candidateButton = candidateButton;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (candidateButton.isSelected()) {
            squarePanel.isNoteMode = true;
            candidateButton.setText(I18nProcessor.getValue("note_on"));
            candidateButton.repaint();
        } else {
            squarePanel.isNoteMode = false;
            candidateButton.setText(I18nProcessor.getValue("note_off"));
        }
    }
}