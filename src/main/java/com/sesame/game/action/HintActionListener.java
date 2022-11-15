package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import com.sesame.game.Sudoku;
import com.sesame.game.ui.SquarePanel;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;

/**
 * Introduction: hint listener
 *
 * @author sesame 2022/10/26
 */
public class HintActionListener implements ActionListener {
    private final Sudoku sudokuFrame;
    private final SquarePanel panel;

    public HintActionListener(Sudoku sudokuFrame, SquarePanel panel) {
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
        if (result.isPresent()) {
            panel.isHintMode = true;
            panel.hintModel = result.get();
            sudokuFrame.getButtonPanel().hintModel(panel.hintModel.getStrategy());

            // 命中解决方案后，直接删除候选数
            if (!panel.hintModel.isCandidateModel()) {
                SolutionModel solutionModel = panel.hintModel.getSolutionModel();
                Position position = solutionModel.getPosition();
                panel.puzzle.deleteCandidate(position.getRow(), position.getCol(),
                    panel.puzzle.getCandidate(position.getRow(), position.getCol()));

            }
            panel.repaint();
        } else {
            if (panel.puzzle.boardFull()) {
                sudokuFrame.getButtonPanel().setUnAvailableLabel(I18nProcessor.getValue("puzzle_finished"));
            } else {
                sudokuFrame.getButtonPanel().setUnAvailableLabel(I18nProcessor.getValue("no_strategy_available"));
            }
            new Thread(new HideTheTextThread(sudokuFrame)).start();
        }
    }

}