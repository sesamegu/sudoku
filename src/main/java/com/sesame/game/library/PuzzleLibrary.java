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
            {"", "", "", "", "", "7", "8", "6", "9"},
            {"4", "5", "", "", "", "", "", "3", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
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
        String[][] array;
        if (gameLevel == GameLevel.EASY) {
            array = new EasyLibrary().caseArray(number);
        } else if (gameLevel == GameLevel.NORMAL) {
            array = new NormalLibrary().caseArray(number);
        } else if (gameLevel == GameLevel.HARD) {
            array = new HardLibrary().caseArray(number);
        } else if (gameLevel == GameLevel.VIP) {
            array = new VipLibrary().caseArray(number);
        } else {
            throw new RuntimeException("should n't be here");
        }

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
