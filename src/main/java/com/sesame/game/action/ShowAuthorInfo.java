package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.sesame.game.Sudoku;
import com.sesame.game.i18n.I18nProcessor;

/**
 * Introduction:show author info
 *
 * @author sesame 2022/11/25
 */
public class ShowAuthorInfo implements ActionListener {
    private final Sudoku sudoku;

    public ShowAuthorInfo(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String title = I18nProcessor.getValue("connect_2_author");
        String message = "<html><body>" + I18nProcessor.getValue("connect_info") + "</body></html>";
        JOptionPane.showMessageDialog(sudoku, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
