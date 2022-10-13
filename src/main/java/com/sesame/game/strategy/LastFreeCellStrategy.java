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
public class LastFreeCellStrategy implements FillStrategy{

    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        Optional<HintModel> rowResult = tryRow(sudokuPuzzle);
        if (rowResult.isPresent()){
            return rowResult;
        }

        // try the column
        // try the grid

        return Optional.empty();

    }

    private Optional<HintModel> tryRow(SudokuPuzzle sudokuPuzzle) {
        // try the rows
        for (int i = 0; i < Const.ROWS; i++) {
            int count = 0;
            int emptyPosition = 0;
            Set<String> copy = new HashSet<>(Const.SET_VALUES);
            List<Position> related = new ArrayList<>(9);
            for (int j = 0; j < Const.COLUMNS; j++) {
                if (sudokuPuzzle.isSlotValid(i, j)) {
                    count++;
                    copy.remove(sudokuPuzzle.getValue(i, j));
                    related.add(new Position(i, j));
                } else {
                    emptyPosition = j;
                }
            }

            if (count == Const.ROWS - 1) {
                Assert.isTrue(copy.size() == 1, "should be one");
                Assert.isTrue(related.size() == 8, "should be eight");

                //位置、值、相关点
                HintModel result = HintModel.build().of(new Position(i, emptyPosition))
                    .of(copy.iterator().next()).of(related);
                return Optional.of(result);
            }
        }
        return Optional.empty();
    }




    @Override
    public String getName() {
        return "Last Free Cell";
    }

    @Override
    public int priority(){
        return 1;
    }

}
