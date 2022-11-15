package com.sesame.game.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.sesame.game.common.Const;
import com.sesame.game.common.SudokuPuzzle;

/**
 * Introduction:brute force solver
 *
 * @author sesame 2022/10/31
 */
public class BruteForceSolver {

    public static Optional<SudokuPuzzle> buildSudokuPuzzle(SudokuPuzzle puzzle) {
        Random randomGenerator = new Random();
        SudokuPuzzle copy = new SudokuPuzzle(puzzle);
        List<String> notUsedValidValues = new ArrayList<>(Arrays.asList(Const.VALID_VALUES));
        for (int r = 0; r < Const.ROWS; r++) {
            int randomValue = randomGenerator.nextInt(notUsedValidValues.size());
            copy.makeMoveWithoutCandidate(r, 0, notUsedValidValues.get(randomValue), true);
            notUsedValidValues.remove(randomValue);
        }

        boolean solved = BruteForceSolver.backtrackSudokuSolver(0, 0, copy);
        if (solved) {
            return Optional.of(copy);
        }
        return Optional.empty();
    }

    /**
     * Solves the sudoku puzzle
     * Pre-cond: r = 0,c = 0
     * Post-cond: solved puzzle
     *
     * @param r: the current row
     * @param c: the current column
     * @return valid move or not or done
     * Responses: Erroneous data
     */
    private static boolean backtrackSudokuSolver(int r, int c, SudokuPuzzle puzzle) {
        //If the move is not valid return false
        puzzle.makeSureInRange(r, c);

        //if the current space is empty
        if (puzzle.isSlotAvailable(r, c)) {

            //loop to find the correct value for the space
            for (int i = 0; i < Const.VALID_VALUES.length; i++) {

                //if the current number works in the space
                if (!puzzle.numInRow(r, Const.VALID_VALUES[i]) && !puzzle.numInCol(c, Const.VALID_VALUES[i])
                    && !puzzle.numInBox(r, c, Const.VALID_VALUES[i])) {

                    //make the move
                    puzzle.makeMoveWithoutCandidate(r, c, Const.VALID_VALUES[i], true);

                    //if puzzle solved return true
                    if (puzzle.boardFull()) {
                        return true;
                    }

                    //go to next move
                    if (r == Const.ROWS - 1) {
                        if (backtrackSudokuSolver(0, c + 1, puzzle)) { return true; }
                    } else {
                        if (backtrackSudokuSolver(r + 1, c, puzzle)) { return true; }
                    }
                }
            }
        }

        //if the current space is not empty
        else {
            //got to the next move
            if (r == Const.ROWS - 1) {
                return backtrackSudokuSolver(0, c + 1, puzzle);
            } else {
                return backtrackSudokuSolver(r + 1, c, puzzle);
            }
        }

        //undo move
        puzzle.makeSlotEmptyWithoutRestCandidate(r, c);

        //backtrack
        return false;
    }
}
