package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import org.springframework.util.CollectionUtils;

/**
 * Introduction: Last Possible Number
 * Check what numbers do the block, vertical column and horizontal row have for the cell, then find out the missing one.
 *
 * @author sesame 2022/10/14
 */
public class LastPossibleNumberStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        for (int row = 0; row < Const.ROWS; row++) {
            for (int column = 0; column < Const.COLUMNS; column++) {
                if (sudokuPuzzle.isSlotValid(row, column)) {
                    continue;
                }

                Set<Position> related = new HashSet<>(27);
                //get the row、column and box digital
                related.addAll(PuzzleTools.getPositionByRow(row));
                related.addAll(PuzzleTools.getPositionByColumn(column));
                related.addAll(PuzzleTools.getPositionByBox(row, column));

                if (CollectionUtils.isEmpty(remaining.get(new Position(row, column)))) {
                    continue;
                }

                List<String> digitalRemain = remaining.get(new Position(row, column));
                if (digitalRemain.size() == 1) {
                    related.remove(new Position(row, column));

                    //build the result
                    SolutionModel solutionModel = new SolutionModel(new Position(row, column), digitalRemain.get(0),
                        new ArrayList<>(related));

                    HintModel result = HintModel.build().of(solutionModel).of(getStrategy());
                    return Optional.of(result);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.LAST_POSSIBLE_NUMBER;
    }

    @Override
    public String buildDesc(HintModel hintModel) {
        SolutionModel solutionModel = hintModel.getSolutionModel();
        Position position = solutionModel.getPosition();
        String solutionDigital = solutionModel.getSolutionDigital();
        return I18nProcessor.getAppendValue(getStrategy().getName() + "_hint", position.getDesc(), solutionDigital);

    }

}
