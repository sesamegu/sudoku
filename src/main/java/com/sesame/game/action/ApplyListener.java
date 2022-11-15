package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import com.sesame.game.Sudoku;
import com.sesame.game.ui.SquarePanel;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;

/**
 * Introduction: do the hint
 *
 * @author sesame 2022/10/26
 */
public class ApplyListener implements ActionListener {
    private final Sudoku sudokuFrame;
    private final SquarePanel panel;

    public ApplyListener(Sudoku sudokuFrame, SquarePanel panel) {
        this.sudokuFrame = sudokuFrame;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (panel.hintModel.isCandidateModel()) {
            CandidateModel candidateModel = panel.hintModel.getCandidateModel();
            Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
            deleteMap.entrySet().forEach(
                one -> panel.puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
            );
        } else {
            SolutionModel solutionModel = panel.hintModel.getSolutionModel();
            panel.puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                solutionModel.getSolutionDigital(),
                true);
            panel.currentlySelectedRow = solutionModel.getPosition().getRow();
            panel.currentlySelectedCol = solutionModel.getPosition().getCol();
        }

        panel.isHintMode = false;
        panel.hintModel = null;
        sudokuFrame.getButtonPanel().buttonModel();
        panel.repaint();
    }
}