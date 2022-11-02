package com.sesame.game;

import java.util.Optional;
import java.util.Random;

import com.sesame.game.library.CaseLibrary;
import com.sesame.game.tool.BruteForceSolver;
import org.springframework.util.Assert;

public class SudokuGenerator {

    public static SudokuPuzzle generateRandomSudoku(GameLevel puzzleType) {
        SudokuPuzzle puzzle = new SudokuPuzzle();
        Optional<SudokuPuzzle> result = BruteForceSolver.buildSudokuPuzzle(puzzle);
        if (!result.isPresent()) {
            throw new RuntimeException("can't solve");
        }
        SudokuPuzzle copy = result.get();

        int numberOfValuesToKeep = (int)(puzzleType.getDifficult() * (Const.ROWS * Const.ROWS));
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

    public static SudokuPuzzle useSpecified(int caseType) {
        Optional<SudokuPuzzle> byCaseType = new CaseLibrary().getByCaseType(caseType);
        if (!byCaseType.isPresent()) {
            throw new RuntimeException("can't find the case " + caseType);
        }

        return byCaseType.get();
    }

    public static SudokuPuzzle useLevelGame(GameLevel gameLevel, int casNumber) {
        SudokuPuzzle aCase = new CaseLibrary().getCase(gameLevel, casNumber);
        Assert.isTrue(aCase != null, "should not be null");
        return aCase;
    }

}
