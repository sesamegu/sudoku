package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.SudokuFrame;
import com.sesame.game.SudokuPanel;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.SolutionModel;

/**
 * Introduction:
 *
 * @author sesame 2022/10/26
 */

public class UnStopHintListener implements ActionListener {
    private final SudokuFrame sudokuFrame;

    private SudokuPanel panel;

    public UnStopHintListener(SudokuFrame sudokuFrame, SudokuPanel panel) {
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
        while (result.isPresent()) {
            HintModel hm = result.get();
            if (hm.isCandidate()) {
                CandidateModel candidateModel = hm.getCandidateModel();
                Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                deleteMap.entrySet().forEach(
                    one -> panel.puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
                );
            } else {
                SolutionModel solutionModel = hm.getSolutionModel();
                panel.puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                    solutionModel.getValue(), true);
            }
            result = StrategyExecute.tryAllStrategy(panel.puzzle);

        }
        panel.repaint();

        sudokuFrame.setUnAvailableLabel(I18nProcessor.getValue("search_finished"));
        new Thread(new HideTheTextThread(sudokuFrame)).start();

    }
}