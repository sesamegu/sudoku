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

        //add easy menu
        JMenu easyMenu = new JMenu("Easy");
        for (int i = 0; i < 10; i++) {
            JMenuItem oneCase = new JMenuItem("Case " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.EASY, (i + 1)));
            easyMenu.add(oneCase);
        }
        menuBar.add(easyMenu);

        //add normal menu
        JMenu normalMenu = new JMenu("Normal");
        for (int i = 0; i < 10; i++) {
            JMenuItem oneCase = new JMenuItem("Case " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.NORMAL, (i + 1)));
            normalMenu.add(oneCase);
        }
        menuBar.add(normalMenu);

        //add hard menu
        JMenu hardMenu = new JMenu("Hard");
        for (int i = 0; i < 10; i++) {
            JMenuItem oneCase = new JMenuItem("Case " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.HARD, (i + 1)));
            hardMenu.add(oneCase);
        }
        menuBar.add(hardMenu);
        //add vip menu
        JMenu vipMenu = new JMenu("Vip");
        for (int i = 0; i < 5; i++) {
            JMenuItem oneCase = new JMenuItem("Case " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.VIP, (i + 1)));
            vipMenu.add(oneCase);
        }
        menuBar.add(vipMenu);

        //Focus
        JMenu focus = new JMenu("Focus");
        menuBar.add(focus);

        JMenuItem solver = new JMenuItem("Solver");
        solver.addActionListener(new LoadGameListener(1));
        focus.add(solver);

        JMenuItem randomGame = new JMenuItem("Random Game");
        randomGame.addActionListener(new NewGameListener(GameLevel.NORMAL));
        focus.add(randomGame);

        // develop
        JMenu loadGame = new JMenu("Develop");
        menuBar.add(loadGame);

        JMenuItem Unstoppable = new JMenuItem("Unstoppable Hint");
        Unstoppable.addActionListener(new UnStopHintListener(this, sPanel));
        loadGame.add(Unstoppable);

        JMenuItem caseTwo = new JMenuItem("Test Game");
        caseTwo.addActionListener(new LoadGameListener(2));
        loadGame.add(caseTwo);

        JMenuItem copy = new JMenuItem("Copy To Clipboard");
        copy.addActionListener(new CopyListener(sPanel));
        loadGame.add(copy);


        this.setJMenuBar(menuBar);
        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800, 600));

        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(150, 500));

        windowPanel.add(sPanel);
        windowPanel.add(buttonSelectionPanel);

        this.add(windowPanel);

        newGameRebuild(GameLevel.NORMAL);
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

    public void newGameRebuild(GameLevel puzzleType) {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.generateRandomSudoku(puzzleType);
        rebuildInterface(generatedPuzzle);
    }

    public void loadGameRebuild(int caseType) {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.useSpecified(caseType);
        rebuildInterface(generatedPuzzle);
    }

    public void loadLevelGameRebuild(GameLevel gameLevel, int caseNumber) {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.useLevelGame(gameLevel, caseNumber);
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

        private final GameLevel puzzleType;

        public NewGameListener(GameLevel puzzleType) {
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

    private class LevelGameListener implements ActionListener {

        private final GameLevel gameLevel;
        private final int caseNumber;

        public LevelGameListener(GameLevel gameLevel, int caseNumber) {
            this.caseNumber = caseNumber;
            this.gameLevel = gameLevel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadLevelGameRebuild(gameLevel, caseNumber);
        }
    }
}
