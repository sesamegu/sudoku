package com.sesame.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
        buttonSelectionPanel.setPreferredSize(new Dimension(150, 500));

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

    public void hintModel() {
        buttonSelectionPanel.removeAll();

        buttonSelectionPanel.setPreferredSize(new Dimension(110, 500));
        //应用按钮
        JButton apply = new JButton("Apply");
        apply.setPreferredSize(new Dimension(110, 40));
        apply.addActionListener(sPanel.new ApplyListener(this));
        buttonSelectionPanel.add(apply);


        JLabel jLabel = new JLabel("技巧名称:");
        jLabel.setPreferredSize(new Dimension(110, 15));
        buttonSelectionPanel.add(jLabel);
        //TODO 改为动态
        JLabel jLabel2 = new JLabel("唯余空白格");
        jLabel2.setPreferredSize(new Dimension(110, 15));
        jLabel2.setForeground(Color.red);
        buttonSelectionPanel.add(jLabel2);

        //提示按钮
        JTextArea textArea = new JTextArea("text");
        textArea.setPreferredSize(new Dimension(110, 200));
        textArea.setLineWrap(true);
        textArea.setEnabled(false);
        //TODO 改为动态
        textArea.setText("一个 3×3 宫、一行或一列中只剩下一个可用单元格，那么我们必须明确缺少 1 到 9 中的哪个数字，并将它填入这个空白单元格");
        buttonSelectionPanel.add(textArea);

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

    public void setUnAvailableLabel(String text){
        unAvailableLabel.setText(text);
    }

}
