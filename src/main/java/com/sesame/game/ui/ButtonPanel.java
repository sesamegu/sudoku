package com.sesame.game.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import com.sesame.game.Sudoku;
import com.sesame.game.action.ApplyListener;
import com.sesame.game.action.CandidateActionListener;
import com.sesame.game.action.DeleteActionListener;
import com.sesame.game.action.HintActionListener;
import com.sesame.game.action.NumActionListener;
import com.sesame.game.common.Const;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.Strategy;

/**
 * Introduction: the button panel
 *
 * @author sesame 2022/11/15
 */
public class ButtonPanel extends JPanel {

    private final Sudoku sudoku;

    private JButton delete;
    private JToggleButton candidateButton;
    private JButton hint;
    private JLabel unAvailableLabel;

    public ButtonPanel(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.setPreferredSize(new Dimension(150, 500));
    }

    public void buttonModel() {
        removeAll();
        setPreferredSize(new Dimension(110, 500));
        //empty Label
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(90, 15));
        add(emptyLabel);

        for (String value : Const.VALID_VALUES) {
            JButton b = new JButton(value);
            b.setPreferredSize(new Dimension(40, 40));
            b.addActionListener(new NumActionListener(sudoku.getSquarePanel()));
            add(b);
        }
        //delete button
        delete = new JButton();
        delete.setPreferredSize(new Dimension(90, 40));
        delete.addActionListener(new DeleteActionListener(sudoku.getSquarePanel()));

        add(delete);
        //note button
        candidateButton = new JToggleButton();
        candidateButton.setPreferredSize(new Dimension(90, 40));
        candidateButton.addChangeListener(new CandidateActionListener(sudoku.getSquarePanel(), candidateButton));

        add(candidateButton);
        //hint button
        hint = new JButton();
        hint.setPreferredSize(new Dimension(90, 40));
        hint.addActionListener(new HintActionListener(sudoku, sudoku.getSquarePanel()));

        add(hint);

        setButtonText();

        //hint text
        unAvailableLabel = new JLabel("");
        unAvailableLabel.setPreferredSize(new Dimension(90, 40));
        add(unAvailableLabel);

        revalidate();
        repaint();
    }

    public void setButtonText() {
        delete.setText(I18nProcessor.getValue("delete"));
        candidateButton.setText(I18nProcessor.getValue("note_off"));
        hint.setText(I18nProcessor.getValue("hint"));
    }

    public void hintModel(Strategy strategy) {
        removeAll();

        setPreferredSize(new Dimension(110, 500));
        //empty Label
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(90, 15));
        add(emptyLabel);

        JLabel jLabel = new JLabel(I18nProcessor.getValue("strategy_name") + ":");
        jLabel.setPreferredSize(new Dimension(110, 15));
        add(jLabel);

        String value = I18nProcessor.getValue(strategy.getName());
        if (value.length() > 15) {
            value = "<html><body>" + value.substring(0, 14) + "<br>" + value.substring(14) + "<body></html>";
        }
        JLabel jLabel2 = new JLabel(value);
        jLabel2.setPreferredSize(new Dimension(110, 30));
        jLabel2.setForeground(Color.red);
        add(jLabel2);

        //hint desc
        JTextArea textArea = new JTextArea("");
        textArea.setPreferredSize(new Dimension(110, 220));
        textArea.setLineWrap(true);
        textArea.setEnabled(false);

        textArea.setText(I18nProcessor.getValue(strategy.getName() + "_desc"));
        add(textArea);

        //apply button
        JButton apply = new JButton(I18nProcessor.getValue("apply"));
        apply.setPreferredSize(new Dimension(110, 40));
        apply.addActionListener(new ApplyListener(sudoku, sudoku.getSquarePanel()));
        add(apply);

        revalidate();
        repaint();
    }

    public void setUnAvailableLabel(String text) {
        unAvailableLabel.setText(text);
    }
}
