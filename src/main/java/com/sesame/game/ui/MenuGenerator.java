//package com.sesame.game.ui;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JMenu;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//
//import com.sesame.game.Sudoku.LoadGameListener;
//import com.sesame.game.Sudoku.NewGameListener;
//import com.sesame.game.action.ChangeLanguageListener;
//import com.sesame.game.action.CopyListener;
//import com.sesame.game.action.ShowCandidateListener;
//import com.sesame.game.action.UnStopHintListener;
//import com.sesame.game.common.GameLevel;
//import com.sesame.game.i18n.I18nProcessor;
//
///**
// * Introduction:menu generator
// *
// * @author sesame 2022/11/15
// */
//public class MenuGenerator {
//
//    public JMenuBar buildJMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //add easy menu
//        JMenu easyMenu = new JMenu(I18nProcessor.getValue("easy"));
//        for (int i = 0; i < 10; i++) {
//            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
//            oneCase.addActionListener(new LevelGameListener(GameLevel.EASY, (i + 1)));
//            easyMenu.add(oneCase);
//        }
//        menuBar.add(easyMenu);
//
//        //add normal menu
//        JMenu normalMenu = new JMenu(I18nProcessor.getValue("normal"));
//        for (int i = 0; i < 10; i++) {
//            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
//            oneCase.addActionListener(new LevelGameListener(GameLevel.NORMAL, (i + 1)));
//            normalMenu.add(oneCase);
//        }
//        menuBar.add(normalMenu);
//
//        //add hard menu
//        JMenu hardMenu = new JMenu(I18nProcessor.getValue("hard"));
//        for (int i = 0; i < 10; i++) {
//            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
//            oneCase.addActionListener(new LevelGameListener(GameLevel.HARD, (i + 1)));
//            hardMenu.add(oneCase);
//        }
//        menuBar.add(hardMenu);
//        //add vip menu
//        JMenu vipMenu = new JMenu(I18nProcessor.getValue("vip"));
//        for (int i = 0; i < 5; i++) {
//            JMenuItem oneCase = new JMenuItem(I18nProcessor.getValue("case") + " " + (i + 1));
//            oneCase.addActionListener(new LevelGameListener(GameLevel.VIP, (i + 1)));
//            vipMenu.add(oneCase);
//        }
//        menuBar.add(vipMenu);
//
//        //Focus
//        JMenu focus = new JMenu(I18nProcessor.getValue("focus"));
//        menuBar.add(focus);
//
//        JMenuItem solver = new JMenuItem(I18nProcessor.getValue("solver"));
//        solver.addActionListener(new LoadGameListener(1));
//        focus.add(solver);
//
//        JMenuItem randomGame = new JMenuItem(I18nProcessor.getValue("random_game"));
//        randomGame.addActionListener(new NewGameListener());
//        focus.add(randomGame);
//
//        JMenuItem language = new JMenuItem(I18nProcessor.getValue("language"));
//        language.addActionListener(new ChangeLanguageListener(this));
//        focus.add(language);
//
//        // develop
//        JMenu loadGame = new JMenu(I18nProcessor.getValue("develop"));
//        menuBar.add(loadGame);
//
//        JMenuItem showCandidate = new JMenuItem(I18nProcessor.getValue("show_candidate"));
//        showCandidate.addActionListener(new ShowCandidateListener(sPanel));
//        loadGame.add(showCandidate);
//
//        JMenuItem Unstoppable = new JMenuItem(I18nProcessor.getValue("unstoppable"));
//        Unstoppable.addActionListener(new UnStopHintListener(this, sPanel));
//        loadGame.add(Unstoppable);
//
//        JMenuItem caseTwo = new JMenuItem(I18nProcessor.getValue("test_game"));
//        caseTwo.addActionListener(new LoadGameListener(2));
//        loadGame.add(caseTwo);
//
//        JMenuItem copy = new JMenuItem(I18nProcessor.getValue("copy_2_clipboard"));
//        copy.addActionListener(new CopyListener(sPanel));
//        loadGame.add(copy);
//        return menuBar;
//    }
//
//    private class LevelGameListener implements ActionListener {
//
//        private final GameLevel gameLevel;
//        private final int caseNumber;
//
//        public LevelGameListener(GameLevel gameLevel, int caseNumber) {
//            this.caseNumber = caseNumber;
//            this.gameLevel = gameLevel;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            loadLevelGameRebuild(gameLevel, caseNumber);
//        }
//    }
//
//}
