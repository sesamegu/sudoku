package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.Sudoku;
import com.sesame.game.ui.SudokuPanel;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;

/**
 * Introduction: do the hint until the end.
 *
 * @author sesame 2022/10/26
 */

public class UnStopHintListener implements ActionListener {
    private final Sudoku sudokuFrame;

    private final SudokuPanel panel;

    public UnStopHintListener(Sudoku sudokuFrame, SudokuPanel panel) {
        this.sudokuFrame = sudokuFrame;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // if user marked the candidate, reset the candidate first.
        if (panel.isUserNoted) {
            panel.puzzle.resetCandidate();
            panel.isUserNoted = false;
        }

        // execute the strategy
        Optional<HintModel> result = StrategyExecute.tryAllStrategy(panel.puzzle);
        while (result.isPresent()) {
            HintModel hm = result.get();
            if (hm.isCandidateModel()) {
                CandidateModel candidateModel = hm.getCandidateModel();
                Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                deleteMap.entrySet().forEach(
                    one -> panel.puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
                );
            } else {
                SolutionModel solutionModel = hm.getSolutionModel();
                panel.puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                    solutionModel.getSolutionDigital(), true);
            }
            result = StrategyExecute.tryAllStrategy(panel.puzzle);

        }
        panel.repaint();

        sudokuFrame.setUnAvailableLabel(I18nProcessor.getValue("search_finished"));
        new Thread(new HideTheTextThread(sudokuFrame)).start();

    }
}