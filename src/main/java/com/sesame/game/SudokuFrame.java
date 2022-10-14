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
import javax.swing.SwingUtilities;

import com.sesame.game.strategy.Strategy;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {

    private final JPanel buttonSelectionPanel;
    private final SudokuPanel sPanel;
    private JLabel unAvailableLabel;

    public SudokuFrame() {
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

        JMenu loadGame = new JMenu("Load Game");
        JMenuItem caseOne = new JMenuItem("puzzle one");
        caseOne.addActionListener(new LoadGameListener(1));
        loadGame.add(caseOne);
        menuBar.add(loadGame);

        this.setJMenuBar(menuBar);


        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800, 600));

        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(150, 500));

        sPanel = new SudokuPanel();

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

    public void loadGameRebuild(int caseType){
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().useSpecified(caseType);
        rebuildInterface(generatedPuzzle);
    }

    private void rebuildInterface(SudokuPuzzle generatedPuzzle) {
        sPanel.newSudokuPuzzle(generatedPuzzle);
        sPanel.setFontSize(Const.FONT_SIZE);
        sPanel.repaint();
        buttonModel();
    }

    public void buttonModel() {
        buttonSelectionPanel.removeAll();
        buttonSelectionPanel.setPreferredSize(new Dimension(110, 500));

        for (String value : Const.VALID_VALUES) {
            JButton b = new JButton(value);
            b.setPreferredSize(new Dimension(40, 40));
            b.addActionListener(sPanel.new NumActionListener());
            buttonSelectionPanel.add(b);
        }
        //删除按钮
        JButton delete = new JButton("delete");
        delete.setPreferredSize(new Dimension(90, 40));
        delete.addActionListener(sPanel.new DeleteActionListener());
        buttonSelectionPanel.add(delete);

        //提示按钮
        JButton hint = new JButton("hint");
        hint.setPreferredSize(new Dimension(90, 40));
        hint.addActionListener(sPanel.new HintActionListener(this));
        buttonSelectionPanel.add(hint);

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
        //应用按钮
        JButton apply = new JButton("应用");
        apply.setPreferredSize(new Dimension(110, 40));
        apply.addActionListener(sPanel.new ApplyListener(this));
        buttonSelectionPanel.add(apply);

        JLabel jLabel = new JLabel("技巧名称:");
        jLabel.setPreferredSize(new Dimension(110, 15));
        buttonSelectionPanel.add(jLabel);

        JLabel jLabel2 = new JLabel(strategy.getName());
        jLabel2.setPreferredSize(new Dimension(110, 15));
        jLabel2.setForeground(Color.red);
        buttonSelectionPanel.add(jLabel2);

        //提示按钮
        JTextArea textArea = new JTextArea("");
        textArea.setPreferredSize(new Dimension(110, 200));
        textArea.setLineWrap(true);
        textArea.setEnabled(false);

        textArea.setText(strategy.getDesc());
        buttonSelectionPanel.add(textArea);

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
