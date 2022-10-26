package com.sesame.game.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sesame.game.Const;
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
        String[][] board = sudokuPanel.getPuzzle().getBoard();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Const.ROWS; i++) {
            sb.append("{");
            for (int j = 0; j < Const.COLUMNS; j++) {
                sb.append("\"");
                sb.append(board[i][j]);
                sb.append("\"");
                if (j != Const.COLUMNS - 1) {
                    sb.append(",");
                }
            }

            if (i != Const.ROWS - 1) {
                sb.append("},\n");
            } else {
                sb.append("}");
            }
        }
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable stringSelection = new StringSelection(sb.toString());
        systemClipboard.setContents(stringSelection, null);
    }
}
