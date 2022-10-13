package com.sesame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    public SudokuPuzzle generateRandomSudoku(SudokuPuzzleType puzzleType) {
        SudokuPuzzle puzzle = new SudokuPuzzle();
        SudokuPuzzle copy = new SudokuPuzzle(puzzle);

        Random randomGenerator = new Random();

        List<String> notUsedValidValues = new ArrayList<>(Arrays.asList(Const.VALID_VALUES));
        for (int r = 0; r < copy.getNumRows(); r++) {
            int randomValue = randomGenerator.nextInt(notUsedValidValues.size());
            copy.makeMove(r, 0, notUsedValidValues.get(randomValue), true);
            notUsedValidValues.remove(randomValue);
        }

        //Bottleneck here need to improve this so that way 16x16 puzzles can be generated
        backtrackSudokuSolver(0, 0, copy);

        int numberOfValuesToKeep = (int)(puzzleType.getDifficult() * (copy.getNumRows() * copy.getNumRows()));

        for (int i = 0; i < numberOfValuesToKeep; ) {
            int randomRow = randomGenerator.nextInt(puzzle.getNumRows());
            int randomColumn = randomGenerator.nextInt(puzzle.getNumColumns());

            if (puzzle.isSlotAvailable(randomRow, randomColumn)) {
                puzzle.makeMove(randomRow, randomColumn, copy.getValue(randomRow, randomColumn), false);
                i++;
            }
        }

        return puzzle;
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
    private boolean backtrackSudokuSolver(int r, int c, SudokuPuzzle puzzle) {
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
                    puzzle.makeMove(r, c, Const.VALID_VALUES[i], true);

                    //if puzzle solved return true
                    if (puzzle.boardFull()) {
                        return true;
                    }

                    //go to next move
                    if (r == puzzle.getNumRows() - 1) {
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
            if (r == puzzle.getNumRows() - 1) {
                return backtrackSudokuSolver(0, c + 1, puzzle);
            } else {
                return backtrackSudokuSolver(r + 1, c, puzzle);
            }
        }

        //undo move
        puzzle.makeSlotEmpty(r, c);

        //backtrack
        return false;
    }
}
