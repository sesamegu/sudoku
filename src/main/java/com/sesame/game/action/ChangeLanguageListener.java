package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;

import com.sesame.game.Sudoku;
import com.sesame.game.i18n.I18nProcessor;

/**
 * Introduction: change language
 *
 * @author sesame 2022/11/14
 */
public class ChangeLanguageListener implements ActionListener {

    private final Sudoku sudoku;

    public ChangeLanguageListener(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        I18nProcessor.setChinese(!I18nProcessor.isChinese());

        //title
        sudoku.setTitle(I18nProcessor.getValue("sudoku"));

        //menu
        JMenuBar menubar = sudoku.getMenuGenerator().buildJMenuBar();
        sudoku.setJMenuBar(menubar);

        //button
        sudoku.getButtonPanel().setButtonText();

        sudoku.revalidate();
    }
}
