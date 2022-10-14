package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.sesame.game.Const;
import com.sesame.game.SudokuPuzzle;

/**
 * Introduction: 唯一候选数
 *
 * @author sesame 2022/10/14
 */
public class LastPossibleNumberStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        for (int row = 0; row < Const.ROWS; row++) {
            for (int column = 0; column < Const.COLUMNS; column++) {
                if (sudokuPuzzle.isSlotValid(row, column)) {
                    continue;
                }

                Set<Position> related = new HashSet<>(27);

                Set<String> contain = new HashSet<>(9);

                //获取这行的所有数字
                for (int k = 0; k < Const.ROWS; k++) {
                    if (sudokuPuzzle.isSlotValid(row, k)) {
                        contain.add(sudokuPuzzle.getValue(row, k));
                    }
                    related.add(new Position(row, k));
                }
                //获取这列的所有数字
                for (int i = 0; i < Const.COLUMNS; i++) {
                    if (sudokuPuzzle.isSlotValid(i, column)) {
                        contain.add(sudokuPuzzle.getValue(i, column));
                    }
                    related.add(new Position(i, column));
                }
                //获取这宫的所有数字
                int rowStart = row - row % 3;
                int columnStart = column - column % 3;
                for (int i = rowStart; i < rowStart + 3; i++) {
                    for (int j = columnStart; j < columnStart + 3; j++) {
                        if (sudokuPuzzle.isSlotValid(i, j)) {
                            contain.add(sudokuPuzzle.getValue(i, j));
                        }
                        related.add(new Position(i, j));
                    }
                }

                //检查结果是否满足
                if (contain.size() == 8) {
                    Set<String> copy = new HashSet<>(Const.SET_VALUES);
                    copy.removeAll(contain);
                    related.remove(new Position(row, column));

                    //位置、值、相关点
                    HintModel result = HintModel.build().of(new Position(row, column))
                        .of(copy.iterator().next()).of(new ArrayList<>(related)).of(getStrategy());
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
    public int priority() {
        return 2;
    }
}
