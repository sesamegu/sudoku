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

    private static Map<Integer,String[][] > library = new HashMap<>(10);

    static {
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
        library.put(1, case1);

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
        //String[][] board = new String[][] {
        //    {"", "4", "6", "9", "", "3", "", "", ""},
        //    {"", "", "3", "", "5", "", "", "6", ""},
        //    {"9", "", "", "", "", "2", "", "", "3"},
        //    {"", "", "5", "", "", "6", "", "", ""},
        //    {"8", "", "", "", "", "", "", "1", ""},
        //    {"", "1", "", "7", "8", "", "2", "", ""},
        //    {"", "", "", "", "", "", "", "5", ""},
        //    {"", "8", "1", "3", "", "", "", "", "7"},
        //    {"", "", "", "8", "", "", "1", "", "4"}
        //};
        //
        //String[][] board = new String[][] {
        //    {"", "", "3", "4", "5", "", "", "8", "7"},
        //    {"4", "5", "6", "", "", "", "", "", ""},
        //    {"7", "9", "8", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""}
        //};

        //String[][] board = new String[][] {
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "2", "6", "", "", "", "", "", ""},
        //    {"", "1", "", "", "2", "", "6", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "5", "", "", "", "", "", "", ""},
        //    {"", "6", "2", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""}
        //};

        //String[][] board = new String[][] {
        //    {"", "4", "", "", "5", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", "1"},
        //    {"", "", "3", "", "", "", "", "", "2"},
        //    {"", "", "", "2", "", "3", "", "", ""},
        //    {"", "", "", "", "", "", "", "3", ""},
        //    {"3", "", "1", "", "", "2", "", "", ""},
        //    {"", "", "", "", "", "1", "", "2", "3"},
        //    {"", "", "2", "", "", "", "", "", ""}
        //};

        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "3", "", "", "", "", ""},
            {"8", "7", "6", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        library.put(2, board);


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
        String[][] strings = library.get(i);
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(strings);
        makeUnmutable(puzzle);
        return Optional.of(puzzle);
    }

}
