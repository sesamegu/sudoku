package com.sesame.game.strategy;

import java.util.List;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mike
 * @date 2022/10/15
 */
public class HiddenSinglesStrategyTest {

    @Test
    public void row_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "8", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "4", "5", "6", "7"},
            {"", "", "", "", "", "", "", "", ""},
            {"", "8", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenSinglesStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();

        Assert.assertTrue(new Position(6, 3).equals(hintModel.getPosition()));
        Assert.assertEquals("8", hintModel.getValue());
        List<Position> related = hintModel.getRelated();
        Assert.assertEquals(10, related.size());

    }

    @Test
    public void column_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "5", "", "", "", ""},
            {"", "", "", "", "4", "", "", "", ""},
            {"", "", "", "", "6", "", "", "", ""},
            {"", "", "", "", "7", "", "", "", ""},
            {"", "8", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "8", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenSinglesStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();

        Assert.assertTrue(new Position(5, 4).equals(hintModel.getPosition()));
        Assert.assertEquals("8", hintModel.getValue());
        List<Position> related = hintModel.getRelated();
        Assert.assertEquals(10, related.size());

    }

    @Test
    public void box_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "8", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "8", "", ""},
            {"", "1", "", "", "", "", "", "", ""},
            {"9", "6", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenSinglesStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();

        Assert.assertTrue(new Position(7, 0).equals(hintModel.getPosition()));
        Assert.assertEquals("8", hintModel.getValue());
        List<Position> related = hintModel.getRelated();
        Assert.assertEquals(10, related.size());

    }
}