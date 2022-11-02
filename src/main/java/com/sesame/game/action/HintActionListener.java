package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import com.sesame.game.SudokuFrame;
import com.sesame.game.SudokuPanel;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.SolutionModel;

/**
 * Introduction:
 *
 * @author sesame 2022/10/26
 */
public class HintActionListener implements ActionListener {
    private final SudokuFrame sudokuFrame;
    private SudokuPanel panel;

    public HintActionListener(SudokuFrame sudokuFrame, SudokuPanel panel) {
        this.sudokuFrame = sudokuFrame;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //如果用户做过标志，需要重置候选数
        if (panel.isUserNoted) {
            panel.puzzle.resetCandidate();
            panel.isUserNoted = false;
        }

        // 执行策略
        Optional<HintModel> result = StrategyExecute.tryAllStrategy(panel.puzzle);
        if (result.isPresent()) {
            panel.isHintMode = true;
            panel.hintModel = result.get();
            sudokuFrame.hintModel(panel.hintModel.getStrategy());

            // 命中解决方案后，直接删除候选数
            if (!panel.hintModel.isCandidate()) {
                SolutionModel solutionModel = panel.hintModel.getSolutionModel();
                Position position = solutionModel.getPosition();
                panel.puzzle.deleteCandidate(position.getRow(), position.getCol(),
                    panel.puzzle.getCandidate(position.getRow(), position.getCol()));

            }
            panel.repaint();
        } else {
            sudokuFrame.setUnAvailableLabel(I18nProcessor.getValue("no_strategy_available"));
            new Thread(new HideTheTextThread(sudokuFrame)).start();
        }
    }

}