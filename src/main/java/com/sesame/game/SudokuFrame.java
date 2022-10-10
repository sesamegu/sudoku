package com.sesame.game;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {

    private final JPanel buttonSelectionPanel;
    private final SudokuPanel sPanel;

    public SudokuFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setMinimumSize(new Dimension(800, 600));

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Game");
        JMenu newGame = new JMenu("New Game");
        JMenuItem sixBySixGame = new JMenuItem("Easy");
        sixBySixGame.addActionListener(new NewGameListener(SudokuPuzzleType.EASY, 26));
        JMenuItem nineByNineGame = new JMenuItem("Normal");
        nineByNineGame.addActionListener(new NewGameListener(SudokuPuzzleType.NORMAL, 26));
        JMenuItem twelveByTwelveGame = new JMenuItem("Hard");
        twelveByTwelveGame.addActionListener(new NewGameListener(SudokuPuzzleType.HARD, 26));

        newGame.add(sixBySixGame);
        newGame.add(nineByNineGame);
        newGame.add(twelveByTwelveGame);
        file.add(newGame);
        menuBar.add(file);
        this.setJMenuBar(menuBar);

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800, 600));

        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(90, 500));

        sPanel = new SudokuPanel();

        windowPanel.add(sPanel);
        windowPanel.add(buttonSelectionPanel);

        this.add(windowPanel);

        rebuildInterface(SudokuPuzzleType.NORMAL, 26);
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

    public void rebuildInterface(SudokuPuzzleType puzzleType, int fontSize) {
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().generateRandomSudoku(puzzleType);
        sPanel.newSudokuPuzzle(generatedPuzzle);
        sPanel.setFontSize(fontSize);
        buttonSelectionPanel.removeAll();
        for (String value : generatedPuzzle.getValidValues()) {
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
        hint.addActionListener(sPanel.new HintActionListener());
        buttonSelectionPanel.add(hint);

        sPanel.repaint();
        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
    }

    private class NewGameListener implements ActionListener {

        private final SudokuPuzzleType puzzleType;
        private final int fontSize;

        public NewGameListener(SudokuPuzzleType puzzleType, int fontSize) {
            this.puzzleType = puzzleType;
            this.fontSize = fontSize;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            rebuildInterface(puzzleType, fontSize);
        }
    }
}
