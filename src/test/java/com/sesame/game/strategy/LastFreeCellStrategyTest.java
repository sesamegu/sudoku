package com.sesame.game.strategy;

import java.util.List;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/13
 */
public class LastFreeCellStrategyTest {

    @Test
    public void row_not_hit_case() {
        String[][] board = new String[][] {
            {"", "", "8", "3", "4", "2", "9", "", ""},
            {"", "", "9", "", "", "", "7", "", ""},
            {"4", "", "", "", "", "", "", "", "3"},
            {"", "", "6", "4", "7", "3", "2", "", ""},
            {"", "3", "", "", "", "", "", "1", ""},
            {"", "", "2", "8", "5", "1", "6", "", ""},
            {"7", "", "", "", "", "", "", "", "8"},
            {"", "", "4", "", "", "", "1", "", ""},
            {"", "", "3", "6", "9", "7", "5", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoardForTest(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void row_hit_case() {
        String[][] board = new String[][] {
            {"7", "1", "8", "3", "4", "2", "9", "", "5"},
            {"", "", "9", "", "", "", "7", "", ""},
            {"4", "", "", "", "", "", "", "", "3"},
            {"", "", "6", "4", "7", "3", "2", "", ""},
            {"", "3", "", "", "", "", "", "1", ""},
            {"", "", "2", "8", "5", "1", "6", "", ""},
            {"7", "", "", "", "", "", "", "", "8"},
            {"", "", "4", "", "", "", "1", "", ""},
            {"", "", "3", "6", "9", "7", "5", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoardForTest(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();

        Assert.assertTrue(new Position(0, 7).equals(hintModel.getPosition()));
        Assert.assertEquals("6", hintModel.getValue());
        List<Position> related = hintModel.getRelated();
        Assert.assertEquals(8, related.size());
        Assert.assertTrue(new Position(0, 0).equals(related.get(0)));
        Assert.assertTrue(new Position(0, 1).equals(related.get(1)));
        Assert.assertTrue(new Position(0, 2).equals(related.get(2)));
        Assert.assertTrue(new Position(0, 3).equals(related.get(3)));
        Assert.assertTrue(new Position(0, 4).equals(related.get(4)));
        Assert.assertTrue(new Position(0, 5).equals(related.get(5)));
        Assert.assertTrue(new Position(0, 6).equals(related.get(6)));
        Assert.assertTrue(new Position(0, 8).equals(related.get(7)));
    }

}