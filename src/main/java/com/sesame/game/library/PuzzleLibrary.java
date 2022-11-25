package com.sesame.game.library;

import java.util.Optional;

import com.sesame.game.common.Const;
import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;

/**
 * Introduction: puzzle library
 *
 * @author sesame 2022/10/14
 */
public class PuzzleLibrary {

    public static Optional<SudokuPuzzle> getByCaseType(int i) {

        String[][] case1 = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };

        String[][] case2 = new String[][] {
            {"", "2", "", "1", "", "6", "", "", ""},
            {"", "", "8", "2", "5", "", "", "", "6"},
            {"1", "", "", "3", "", "", "", "4", ""},
            {"9", "", "", "", "", "", "", "5", ""},
            {"", "4", "", "", "8", "", "9", "", ""},
            {"", "", "6", "", "", "2", "", "", ""},
            {"", "", "1", "", "", "", "8", "", "4"},
            {"", "", "", "9", "", "4", "", "", ""},
            {"7", "", "4", "", "", "", "2", "3", ""},
        };

        SudokuPuzzle puzzle = new SudokuPuzzle();
        if (i == 1) {
            puzzle.setBoard(case1);
        } else if (i == 2) {
            puzzle.setBoard(case2);
        }

        makeUnMutable(puzzle);
        return Optional.of(puzzle);
    }

    public static SudokuPuzzle getCase(GameLevel gameLevel, int number) {
        String[][] array = FileLibrary.caseArray(number, gameLevel);
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(array);
        makeUnMutable(puzzle);

        return puzzle;

    }

    private static void makeUnMutable(SudokuPuzzle puzzle) {
        for (int i = 0; i < Const.ROWS; i++) {
            for (int j = 0; j < Const.COLUMNS; j++) {
                if (puzzle.isSlotValid(i, j)) {
                    puzzle.makeMutable(i, j, false);
                }

            }
        }
    }

}
