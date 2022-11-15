package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;

/**
 * Introduction: the abstract class when the strategy is based on the basic unit（row、column、box）
 *
 * @author sesame 2022/10/19
 */
public abstract class AbstractUnitStrategy implements FillStrategy {

    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();

        Optional<HintModel> byRow = findByRow(remaining);
        if (byRow.isPresent()) {
            return byRow;
        }

        Optional<HintModel> byColumn = findByColumn(remaining);
        if (byColumn.isPresent()) {
            return byColumn;
        }

        Optional<HintModel> byBox = findByBox(remaining);
        if (byBox.isPresent()) {
            return byBox;
        }

        return Optional.empty();
    }

    private Optional<HintModel> findByRow(Map<Position, List<String>> remaining) {
        for (int row = 0; row < Const.ROWS; row++) {

            List<Position> positionList = PuzzleTools.getPositionByRow(row);
            Optional<HintModel> result = processBasicUnit(remaining, positionList);
            if (result.isPresent()) {
                List<UnitModel> unitModelList = new ArrayList<>();
                unitModelList.add(UnitModel.buildFromRow(row));
                result.get().of(unitModelList);

                return result;
            }
        }
        return Optional.empty();
    }

    private Optional<HintModel> findByColumn(Map<Position, List<String>> remaining) {
        for (int column = 0; column < Const.COLUMNS; column++) {
            List<Position> positionList = PuzzleTools.getPositionByColumn(column);
            Optional<HintModel> result = processBasicUnit(remaining, positionList);
            if (result.isPresent()) {
                List<UnitModel> unitModelList = new ArrayList<>();
                unitModelList.add(UnitModel.buildFromColumn(column));
                result.get().of(unitModelList);

                return result;
            }

        }
        return Optional.empty();
    }

    private Optional<HintModel> findByBox(Map<Position, List<String>> remaining) {
        for (int row = 0; row < Const.ROWS; row = row + Const.BOX_WIDTH) {
            for (int column = 0; column < Const.COLUMNS; column = column + Const.BOX_WIDTH) {
                List<Position> positionList = PuzzleTools.getPositionByBox(row, column);
                Optional<HintModel> result = processBasicUnit(remaining, positionList);
                if (result.isPresent()) {
                    List<UnitModel> unitModelList = new ArrayList<>();
                    unitModelList.add(UnitModel.buildFromBox(row, column));
                    result.get().of(unitModelList);

                    return result;
                }
            }
        }

        return Optional.empty();
    }

    /**
     * the process of the unit
     *
     * @param remaining
     * @param positionList
     * @return
     */
    protected abstract Optional<HintModel> processBasicUnit(Map<Position, List<String>> remaining,
        List<Position> positionList);

}
