package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sesame.game.Const;
import com.sesame.game.SudokuPuzzle;
import org.springframework.util.Assert;

/**
 * Introduction:最后一个空白格
 *
 * @author sesame 2022/10/12
 */
public class LastFreeCellStrategy implements FillStrategy {

    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
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

                //位置、值、相关点
                HintModel result = HintModel.build().of(new Position(i, emptyColumnPosition))
                    .of(copy.iterator().next()).of(related).of(getStrategy());
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

                //位置、值、相关点
                HintModel result = HintModel.build().of(new Position(emptyRowPosition, i))
                    .of(copy.iterator().next()).of(related).of(getStrategy());
                return Optional.of(result);
            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> tryBox(SudokuPuzzle sudokuPuzzle) {
        //获取每个宫的起点
        for (int rowStartPoint = 0; rowStartPoint < Const.ROWS; rowStartPoint = rowStartPoint + Const.BOX_WIDTH) {
            for (int columnStartPoint = 0; columnStartPoint < Const.COLUMNS; columnStartPoint = columnStartPoint + Const.BOX_WIDTH) {
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

            //位置、值、相关点
            HintModel result = HintModel.build().of(new Position(rowPosition, columnPosition))
                .of(copy.iterator().next()).of(related).of(getStrategy());
            return Optional.of(result);
        }

        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.LAST_FREE_CELL;
    }

    @Override
    public int priority() {
        return 1;
    }

}
