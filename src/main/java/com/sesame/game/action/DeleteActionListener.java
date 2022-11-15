package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sesame.game.ui.SquarePanel;

/**
 * Introduction: delete the cell
 *
 * @author sesame 2022/10/26
 */
public class DeleteActionListener implements ActionListener {

    private final SquarePanel panel;

    public DeleteActionListener(SquarePanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (panel.currentlySelectedCol != -1 && panel.currentlySelectedRow != -1) {
            if (panel.puzzle.isSlotMutable(panel.currentlySelectedRow, panel.currentlySelectedCol) && panel.puzzle
                .isSlotValid(
                    panel.currentlySelectedRow, panel.currentlySelectedCol)) {
                panel.puzzle.makeSlotEmpty(panel.currentlySelectedRow, panel.currentlySelectedCol);
            }
            panel.repaint();
        }
    }
}