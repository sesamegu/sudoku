package com.sesame.game;

import java.util.Optional;

/**
 * Introduction: 题库
 *
 * @author sesame 2022/10/14
 */
public class CaseLibrary {
    static {
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

        //String[][] board = new String[][] {
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "3", "", "", "", "", ""},
        //    {"8", "7", "6", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""},
        //    {"", "", "", "", "", "", "", "", ""}
        //};

        // ios 数独专家2
        //{"","","","","2","","","8","5"},
        //{"6","","2","","7","","","",""},
        //{"3","","","","","","","1",""},
        //{"7","","","9","","4","","",""},
        //{"","4","","","","","","3",""},
        //{"","","9","","","5","","2",""},
        //{"","","5","","","","2","6",""},
        //{"2","","","","8","6","1","",""},
        //{"","3","","","","2","","7",""}

    }

    private static void makeUnmutable(SudokuPuzzle puzzle) {
        for (int i = 0; i < Const.ROWS; i++) {
            for (int j = 0; j < Const.COLUMNS; j++) {
                if (puzzle.isSlotValid(i, j)) {
                    puzzle.makeMutable(i, j, false);
                }

            }
        }
    }

    public Optional<SudokuPuzzle> getByCaseType(int i) {

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

        //String[][] case2 = new String[][] {
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

        String[][] case2 = new String[][] {
            {"9", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "4", "", "", ""},
            {"8", "", "", "", "", "3", "", "", "1"},
            {"7", "", "", "", "", "2", "", "", "8"},
            {"3", "", "", "6", "", "5", "", "", "4"},
            {"5", "", "", "", "", "", "", "", "7"},
            {"1", "", "", "", "", "9", "", "", ""},
            {"", "", "", "", "", "", "", "6", ""},
            {"", "", "", "", "", "", "", "", "9"}
        };

        SudokuPuzzle puzzle = new SudokuPuzzle();
        if (i == 1) {
            puzzle.setBoard(case1);
        } else if (i == 2) {
            puzzle.setBoard(case2);
        }

        makeUnmutable(puzzle);
        return Optional.of(puzzle);
    }

}
