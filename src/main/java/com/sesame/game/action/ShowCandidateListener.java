package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sesame.game.SudokuPanel;

/**
 * Introduction:show candidate switch
 *
 * @author sesame 2022/11/8
 */
public class ShowCandidateListener implements ActionListener {

    private final SudokuPanel panel;

    public ShowCandidateListener(SudokuPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.showCandidate = !panel.showCandidate;
        panel.repaint();
    }
}
