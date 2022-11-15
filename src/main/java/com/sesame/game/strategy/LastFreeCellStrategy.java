package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sesame.game.common.Const;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.Assert;

/**
 * Introduction:Last Free Cell
 * There is only one free cell left in the block, vertical column or horizontal row, then we have to define which number
 * from 1 to 9 is missing
 *
 * @author sesame 2022/10/12
 */
public class LastFreeCellStrategy implements FillStrategy {

    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Optional<HintModel> rowResult = tryRow(sudokuPuzzle);
        if (rowResult.isPresent()) {
            return rowResult;
        }

        Optional<HintModel> columnModel = tryColumn(sudokuPuzzle);
        if (columnModel.isPresent()) {
            return columnModel;
        }

        return tryBox(sudokuPuzzle);
    }

    private Optional<HintModel> tryRow(SudokuPuzzle sudokuPuzzle) {
        // try the rows
        for (int i = 0; i < Const.ROWS; i++) {
            int count = 0;
            int emptyColumnPosition = 0;
            Set<String> copy = new HashSet<>(Const.SET_VALUES);
            List<Position> related = new ArrayList<>(9);
            for (int j = 0; j < Const.COLUMNS; j++) {
                if (sudokuPuzzle.isSlotValid(i, j)) {
                    count++;
                    copy.remove(sudokuPuzzle.getValue(i, j));
                    related.add(new Position(i, j));
                } else {
                    emptyColumnPosition = j;
                }
            }

            if (count == Const.ROWS - 1) {
                Assert.isTrue(copy.size() == 1, "should be one");
                Assert.isTrue(related.size() == 8, "should be eight");

                //build the result
                SolutionModel solutionModel = new SolutionModel(new Position(i, emptyColumnPosition),
                    copy.iterator().next(), related);
                HintModel result = HintModel.build().of(solutionModel).of(getStrategy());

                List<UnitModel> unitModelList = new ArrayList<>();
                unitModelList.add(UnitModel.buildFromRow(i));
                result.of(unitModelList);

                return Optional.of(result);
            }
        }
        return Optional.empty();
    }

    private Optional<HintModel> tryColumn(SudokuPuzzle sudokuPuzzle) {
        for (int i = 0; i < Const.COLUMNS; i++) {
            int count = 0;
            int emptyRowPosition = 0;
            Set<String> copy = new HashSet<>(Const.SET_VALUES);
            List<Position> related = new ArrayList<>(9);
            for (int j = 0; j < Const.ROWS; j++) {
                if (sudokuPuzzle.isSlotValid(j, i)) {
                    count++;
                    copy.remove(sudokuPuzzle.getValue(j, i));
                    related.add(new Position(j, i));
                } else {
                    emptyRowPosition = j;
                }
            }

            if (count == Const.COLUMNS - 1) {
                Assert.isTrue(copy.size() == 1, "should be one");
                Assert.isTrue(related.size() == 8, "should be eight");

                //build the result
                SolutionModel solutionModel = new SolutionModel(new Position(emptyRowPosition, i),
                    copy.iterator().next(), related);
                HintModel result = HintModel.build().of(solutionModel).of(getStrategy());

                List<UnitModel> unitModelList = new ArrayList<>();
                unitModelList.add(UnitModel.buildFromColumn(i));
                result.of(unitModelList);

                return Optional.of(result);
            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> tryBox(SudokuPuzzle sudokuPuzzle) {
        //iterate the box
        for (int rowStartPoint = 0; rowStartPoint < Const.ROWS; rowStartPoint = rowStartPoint + Const.BOX_WIDTH) {
            for (int columnStartPoint = 0; columnStartPoint < Const.COLUMNS;
                columnStartPoint = columnStartPoint + Const.BOX_WIDTH) {
                Optional<HintModel> result = checkEveryBox(sudokuPuzzle, rowStartPoint, columnStartPoint);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> checkEveryBox(SudokuPuzzle sudokuPuzzle, int rowStartPoint, int columnStartPoint) {
        int count = 0;
        int rowPosition = 0;
        int columnPosition = 0;
        Set<String> copy = new HashSet<>(Const.SET_VALUES);
        List<Position> related = new ArrayList<>(9);

        for (int row = rowStartPoint; row < rowStartPoint + Const.BOX_WIDTH; row++) {
            for (int column = columnStartPoint; column < columnStartPoint + Const.BOX_WIDTH; column++) {
                if (sudokuPuzzle.isSlotValid(row, column)) {
                    count++;
                    copy.remove(sudokuPuzzle.getValue(row, column));
                    related.add(new Position(row, column));
                } else {
                    rowPosition = row;
                    columnPosition = column;
                }
            }
        }

        if (count == Const.COLUMNS - 1) {
            Assert.isTrue(copy.size() == 1, "should be one");
            Assert.isTrue(related.size() == 8, "should be eight");

            //build the result
            SolutionModel solutionModel = new SolutionModel(new Position(rowPosition, columnPosition),
                copy.iterator().next(), related);

            HintModel result = HintModel.build().of(solutionModel).of(getStrategy());

            List<UnitModel> unitModelList = new ArrayList<>();
            unitModelList.add(UnitModel.buildFromBox(rowStartPoint, columnStartPoint));
            result.of(unitModelList);

            return Optional.of(result);
        }

        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.LAST_FREE_CELL;
    }

}
