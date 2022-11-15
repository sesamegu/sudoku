package com.sesame.game.action;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.sesame.game.common.Const;
import com.sesame.game.ui.SudokuPanel;

/**
 * Introduction: mouse adapter
 *
 * @author sesame 2022/10/26
 */
public class SudokuPanelMouseAdapter extends MouseInputAdapter {
    private final SudokuPanel panel;

    public SudokuPanelMouseAdapter(SudokuPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int slotWidth = panel.usedWidth / Const.COLUMNS;
            int slotHeight = panel.usedHeight / Const.ROWS;
            panel.currentlySelectedRow = e.getY() / slotHeight;
            panel.currentlySelectedCol = e.getX() / slotWidth;
            e.getComponent().repaint();
        }
    }
}
