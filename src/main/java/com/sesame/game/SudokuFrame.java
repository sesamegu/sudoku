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
import com.sesame.game.action.ShowCandidateListener;
import com.sesame.game.action.UnStopHintListener;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.Strategy;
import com.sesame.game.tool.SudokuGenerator;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {

    private final JPanel buttonSelectionPanel;
    private final SudokuPanel sPanel;
    private JLabel unAvailableLabel;

    public SudokuFrame() {
        sPanel = new SudokuPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(I18nProcessor.getValue("sudoku"));
        this.setMinimumSize(new Dimension(800, 600));

        JMenuBar menuBar = new JMenuBar();

        //add easy menu
        JMenu easyMenu = new JMenu(I18nProcessor.getValue("easy"));
        for (int i = 0; i < 10; i++) {
            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.EASY, (i + 1)));
            easyMenu.add(oneCase);
        }
        menuBar.add(easyMenu);

        //add normal menu
        JMenu normalMenu = new JMenu(I18nProcessor.getValue("normal"));
        for (int i = 0; i < 10; i++) {
            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.NORMAL, (i + 1)));
            normalMenu.add(oneCase);
        }
        menuBar.add(normalMenu);

        //add hard menu
        JMenu hardMenu = new JMenu(I18nProcessor.getValue("hard"));
        for (int i = 0; i < 10; i++) {
            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.HARD, (i + 1)));
            hardMenu.add(oneCase);
        }
        menuBar.add(hardMenu);
        //add vip menu
        JMenu vipMenu = new JMenu(I18nProcessor.getValue("vip"));
        for (int i = 0; i < 5; i++) {
            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
            oneCase.addActionListener(new LevelGameListener(GameLevel.VIP, (i + 1)));
            vipMenu.add(oneCase);
        }
        menuBar.add(vipMenu);

        //Focus
        JMenu focus = new JMenu(I18nProcessor.getValue("focus"));
        menuBar.add(focus);

        JMenuItem solver = new JMenuItem(I18nProcessor.getValue("solver"));
        solver.addActionListener(new LoadGameListener(1));
        focus.add(solver);

        JMenuItem randomGame = new JMenuItem(I18nProcessor.getValue("random_game"));
        randomGame.addActionListener(new NewGameListener());
        focus.add(randomGame);

        // develop
        JMenu loadGame = new JMenu(I18nProcessor.getValue("develop"));
        menuBar.add(loadGame);

        JMenuItem showCandidate = new JMenuItem(I18nProcessor.getValue("show_candidate"));
        showCandidate.addActionListener(new ShowCandidateListener(sPanel));
        loadGame.add(showCandidate);

        JMenuItem Unstoppable = new JMenuItem(I18nProcessor.getValue("unstoppable"));
        Unstoppable.addActionListener(new UnStopHintListener(this, sPanel));
        loadGame.add(Unstoppable);

        JMenuItem caseTwo = new JMenuItem(I18nProcessor.getValue("test_game"));
        caseTwo.addActionListener(new LoadGameListener(2));
        loadGame.add(caseTwo);

        JMenuItem copy = new JMenuItem(I18nProcessor.getValue("copy_2_clipboard"));
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

        loadLevelGameRebuild(GameLevel.EASY, 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuFrame frame = new SudokuFrame();
            frame.setVisible(true);
        });
    }

    public void newGameRebuild() {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.generateRandomSudoku();
        rebuildInterface(generatedPuzzle, true);
    }

    public void loadGameRebuild(int caseType) {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.useSpecified(caseType);
        rebuildInterface(generatedPuzzle, true);
    }

    public void loadLevelGameRebuild(GameLevel gameLevel, int caseNumber) {
        SudokuPuzzle generatedPuzzle = SudokuGenerator.useLevelGame(gameLevel, caseNumber);
        if (gameLevel == GameLevel.EASY) {
            rebuildInterface(generatedPuzzle, false);
        } else {
            rebuildInterface(generatedPuzzle, true);
        }
    }

    private void rebuildInterface(SudokuPuzzle generatedPuzzle, boolean showCandidate) {
        sPanel.newSudokuPuzzle(generatedPuzzle, showCandidate);
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
        //delete button
        JButton delete = new JButton(I18nProcessor.getValue("delete"));
        delete.setPreferredSize(new Dimension(90, 40));
        delete.addActionListener(new DeleteActionListener(sPanel));
        buttonSelectionPanel.add(delete);

        JToggleButton candidateButton = new JToggleButton(I18nProcessor.getValue("note_off"));
        candidateButton.setPreferredSize(new Dimension(90, 40));
        candidateButton.addChangeListener(new CandidateActionListener(sPanel, candidateButton));
        buttonSelectionPanel.add(candidateButton);

        //hint button
        JButton hint = new JButton(I18nProcessor.getValue("hint"));
        hint.setPreferredSize(new Dimension(90, 40));
        hint.addActionListener(new HintActionListener(this, sPanel));
        buttonSelectionPanel.add(hint);

        //hint text
        unAvailableLabel = new JLabel("");
        unAvailableLabel.setPreferredSize(new Dimension(90, 40));
        buttonSelectionPanel.add(unAvailableLabel);

        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
    }

    public void hintModel(Strategy strategy) {
        buttonSelectionPanel.removeAll();

        buttonSelectionPanel.setPreferredSize(new Dimension(110, 500));

        JLabel jLabel = new JLabel(I18nProcessor.getValue("strategy_name") + ":");
        jLabel.setPreferredSize(new Dimension(110, 15));
        buttonSelectionPanel.add(jLabel);

        String value = I18nProcessor.getValue(strategy.getName());
        if (value.length() > 15) {
            value = "<html><body>" + value.substring(0, 14) + "<br>" + value.substring(14) + "<body></html>";
        }
        JLabel jLabel2 = new JLabel(value);
        jLabel2.setPreferredSize(new Dimension(110, 30));
        jLabel2.setForeground(Color.red);
        buttonSelectionPanel.add(jLabel2);

        //hint desc
        JTextArea textArea = new JTextArea("");
        textArea.setPreferredSize(new Dimension(110, 220));
        textArea.setLineWrap(true);
        textArea.setEnabled(false);

        textArea.setText(I18nProcessor.getValue(strategy.getName() + "_desc"));
        buttonSelectionPanel.add(textArea);

        //apply button
        JButton apply = new JButton(I18nProcessor.getValue("apply"));
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

        @Override
        public void actionPerformed(ActionEvent e) {
            newGameRebuild();
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
