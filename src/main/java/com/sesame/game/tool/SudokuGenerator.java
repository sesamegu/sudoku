package com.sesame.game.tool;

import com.sesame.game.common.Const;
import com.sesame.game.common.SudokuPuzzle;

import java.util.Optional;
import java.util.Random;

public class SudokuGenerator {

    public static SudokuPuzzle generateRandomSudoku() {
        SudokuPuzzle puzzle = new SudokuPuzzle();
        Optional<SudokuPuzzle> result = BruteForceSolver.buildSudokuPuzzle(puzzle);
        if (!result.isPresent()) {
            throw new RuntimeException("can't solve");
        }
        SudokuPuzzle copy = result.get();

        int numberOfValuesToKeep = (int) (0.45000f * (Const.ROWS * Const.ROWS));
        Random randomGenerator = new Random();
        for (int i = 0; i < numberOfValuesToKeep; ) {
            int randomRow = randomGenerator.nextInt(Const.ROWS);
            int randomColumn = randomGenerator.nextInt(Const.COLUMNS);

            if (puzzle.isSlotAvailable(randomRow, randomColumn)) {
                puzzle.makeMoveWithoutCandidate(randomRow, randomColumn, copy.getValue(randomRow, randomColumn), false);
                i++;
            }
        }

        puzzle.resetCandidate();

        return puzzle;
    }

}
