package com.sesame.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.sesame.game.action.ApplyListener;
import com.sesame.game.action.CandidateActionListener;
import com.sesame.game.action.CopyListener;
import com.sesame.game.action.DeleteActionListener;
import com.sesame.game.action.HintActionListener;
import com.sesame.game.action.NumActionListener;
import com.sesame.game.action.UnStopHintListener;
import com.sesame.game.strategy.Strategy;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {

    private final JPanel buttonSelectionPanel;
    private final SudokuPanel sPanel;
    private JLabel unAvailableLabel;

    public SudokuFrame() {
        sPanel = new SudokuPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setMinimumSize(new Dimension(800, 600));

        JMenuBar menuBar = new JMenuBar();
        JMenu newGame = new JMenu("New Game");
        JMenuItem sixBySixGame = new JMenuItem("Easy");
        sixBySixGame.addActionListener(new NewGameListener(SudokuPuzzleType.EASY));
        JMenuItem nineByNineGame = new JMenuItem("Normal");
        nineByNineGame.addActionListener(new NewGameListener(SudokuPuzzleType.NORMAL));
        JMenuItem twelveByTwelveGame = new JMenuItem("Hard");
        twelveByTwelveGame.addActionListener(new NewGameListener(SudokuPuzzleType.HARD));

        newGame.add(sixBySixGame);
        newGame.add(nineByNineGame);
        newGame.add(twelveByTwelveGame);
        menuBar.add(newGame);

        JMenu loadGame = new JMenu("Develop");
        JMenuItem caseOne = new JMenuItem("自由练习场");
        caseOne.addActionListener(new LoadGameListener(1));
        loadGame.add(caseOne);

        JMenuItem caseTwo = new JMenuItem("测试局");
        caseTwo.addActionListener(new LoadGameListener(2));
        loadGame.add(caseTwo);

        JMenuItem copy = new JMenuItem("复制到剪切板");
        copy.addActionListener(new CopyListener(sPanel));
        loadGame.add(copy);

        menuBar.add(loadGame);

        this.setJMenuBar(menuBar);

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800, 600));

        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(150, 500));

        windowPanel.add(sPanel);
        windowPanel.add(buttonSelectionPanel);

        this.add(windowPanel);

        newGameRebuild(SudokuPuzzleType.NORMAL);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuFrame frame = new SudokuFrame();
                frame.setVisible(true);
            }
        });
    }

    public void newGameRebuild(SudokuPuzzleType puzzleType) {
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().generateRandomSudoku(puzzleType);
        rebuildInterface(generatedPuzzle);
    }

    public void loadGameRebuild(int caseType) {
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().useSpecified(caseType);
        rebuildInterface(generatedPuzzle);
    }

    private void rebuildInterface(SudokuPuzzle generatedPuzzle) {
        sPanel.newSudokuPuzzle(generatedPuzzle);
        sPanel.repaint();
        buttonModel();
    }

    public void buttonModel() {
        buttonSelectionPanel.removeAll();
        buttonSelectionPanel.setPreferredSize(new Dimension(110, 500));

        for (String value : Const.VALID_VALUES) {
            JButton b = new JButton(value);
            b.setPreferredSize(new Dimension(40, 40));
            b.addActionListener(new NumActionListener(sPanel));
            buttonSelectionPanel.add(b);
        }
        //删除按钮
        JButton delete = new JButton("Delete");
        delete.setPreferredSize(new Dimension(90, 40));
        delete.addActionListener(new DeleteActionListener(sPanel));
        buttonSelectionPanel.add(delete);

        JToggleButton candidateButton = new JToggleButton("Note Off");
        candidateButton.setPreferredSize(new Dimension(90, 40));
        candidateButton.addChangeListener(new CandidateActionListener(sPanel, candidateButton));
        buttonSelectionPanel.add(candidateButton);

        //提示按钮
        JButton hint = new JButton("Hint");
        hint.setPreferredSize(new Dimension(90, 40));
        hint.addActionListener(new HintActionListener(this, sPanel));
        buttonSelectionPanel.add(hint);

        //无尽提示按钮
        JButton Unstop = new JButton("Unstop Hint");
        Unstop.setPreferredSize(new Dimension(90, 40));
        Unstop.addActionListener(new UnStopHintListener(this, sPanel));
        buttonSelectionPanel.add(Unstop);

        //无可用提示文案
        unAvailableLabel = new JLabel("");
        unAvailableLabel.setPreferredSize(new Dimension(90, 40));
        buttonSelectionPanel.add(unAvailableLabel);

        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
    }

    public void hintModel(Strategy strategy) {
        buttonSelectionPanel.removeAll();

        buttonSelectionPanel.setPreferredSize(new Dimension(110, 500));

        JLabel jLabel = new JLabel("技巧名称:");
        jLabel.setPreferredSize(new Dimension(110, 15));
        buttonSelectionPanel.add(jLabel);

        JLabel jLabel2 = new JLabel(strategy.getName());
        jLabel2.setPreferredSize(new Dimension(110, 15));
        jLabel2.setForeground(Color.red);
        buttonSelectionPanel.add(jLabel2);

        //提示按钮
        JTextArea textArea = new JTextArea("");
        textArea.setPreferredSize(new Dimension(110, 220));
        textArea.setLineWrap(true);
        textArea.setEnabled(false);

        textArea.setText(strategy.getDesc());
        buttonSelectionPanel.add(textArea);

        //应用按钮
        JButton apply = new JButton("应用");
        apply.setPreferredSize(new Dimension(110, 40));
        apply.addActionListener(new ApplyListener(this, sPanel));
        buttonSelectionPanel.add(apply);

        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
    }

    public void setUnAvailableLabel(String text) {
        unAvailableLabel.setText(text);
    }

    private class NewGameListener implements ActionListener {

        private final SudokuPuzzleType puzzleType;

        public NewGameListener(SudokuPuzzleType puzzleType) {
            this.puzzleType = puzzleType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            newGameRebuild(puzzleType);
        }
    }

    private class LoadGameListener implements ActionListener {

        private final int caseType;

        public LoadGameListener(int caseType) {
            this.caseType = caseType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadGameRebuild(caseType);
        }
    }

}
