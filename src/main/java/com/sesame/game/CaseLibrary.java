package com.sesame.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Introduction: 题库
 *
 * @author sesame 2022/10/14
 */
public class CaseLibrary {

    private static Map<Integer, SudokuPuzzle> library = new HashMap<>(10);

    static {
        String[][] case1 = new String[][] {
            {"", "1", "", "", "", "8", "5", "7", "9"},
            {"4", "2", "", "1", "5", "9", "", "6", ""},
            {"", "5", "8", "", "", "6", "1", "4", "2"},
            {"", "3", "2", "", "", "7", "6", "9", "5"},
            {"5", "", "", "", "", "1", "8", "2", "3"},
            {"6", "8", "9", "5", "", "", "", "", "7"},
            {"", "", "", "8", "3", "", "", "", "6"},
            {"", "", "3", "9", "1", "", "7", "", ""},
            {"8", "", "", "", "6", "4", "", "3", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(case1);
        library.put(1, puzzle);

        makeUnmutable(puzzle);

        //String[][] board = new String[][] {
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "8", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "8", "", ""},
        //    {"", "1", "", "", "", "", "", "", ""},
        //    {"9", "6", "", "", "", "", "", "", ""}
        //};
        String[][] board = new String[][] {
            {"", "4", "6", "9", "", "3", "", "", ""},
            {"", "", "3", "", "5", "", "", "6", ""},
            {"9", "", "", "", "", "2", "", "", "3"},
            {"", "", "5", "", "", "6", "", "", ""},
            {"8", "", "", "", "", "", "", "1", ""},
            {"", "1", "", "7", "8", "", "2", "", ""},
            {"", "", "", "", "", "", "", "5", ""},
            {"", "8", "1", "3", "", "", "", "", "7"},
            {"", "", "", "8", "", "", "1", "", "4"}
        };
        SudokuPuzzle puzzle2 = new SudokuPuzzle();
        puzzle2.setBoard(board);
        makeUnmutable(puzzle2);
        library.put(2, puzzle2);


    }

    private static void makeUnmutable(SudokuPuzzle puzzle) {
        for (int i = 0; i < Const.ROWS; i++) {
            for (int j = 0; j < Const.COLUMNS; j++) {
                if (puzzle.isSlotValid(i, j)) {
                    puzzle. makeMutable(i,j,false);
                }

            }
        }
    }

    public static Optional<SudokuPuzzle> getByCaseType(int i) {
        SudokuPuzzle sudokuPuzzle = library.get(i);
        return Optional.of(sudokuPuzzle);
    }

}
