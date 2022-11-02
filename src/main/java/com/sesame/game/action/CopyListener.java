package com.sesame.game.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sesame.game.SudokuPanel;

/**
 * Introduction:
 *
 * @author sesame 2022/10/26
 */

public class CopyListener implements ActionListener {
    private SudokuPanel sudokuPanel;

    public CopyListener(SudokuPanel sudokuPanel) {
        this.sudokuPanel = sudokuPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String data = sudokuPanel.puzzle.printBoard();
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable stringSelection = new StringSelection(data);
        systemClipboard.setContents(stringSelection, null);
    }
}
