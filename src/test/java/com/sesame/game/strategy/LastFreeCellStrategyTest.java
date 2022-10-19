package com.sesame.game.strategy;

import java.util.List;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.SolutionModel;
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
        puzzle.setBoard(board);
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
        puzzle.setBoard(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(0, 7).equals(solutionModel.getPosition()));
        Assert.assertEquals("6", solutionModel.getValue());
        List<Position> related = solutionModel.getRelated();
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


    @Test
    public void column_hit_case() {
        String[][] board = new String[][] {
            {"7", "1", "8", "3", "4", "2", "", "", "5"},
            {"", "", "9", "", "", "", "7", "", "2"},
            {"4", "", "", "", "", "", "", "", "3"},
            {"", "", "6", "4", "7", "3", "2", "", "1"},
            {"", "3", "", "", "", "", "", "1", "6"},
            {"", "", "2", "8", "5", "1", "6", "", ""},
            {"7", "", "", "", "", "", "", "", "8"},
            {"", "", "4", "", "", "", "1", "", "9"},
            {"", "", "3", "6", "9", "7", "5", "", "4"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();


        Assert.assertTrue(new Position(5, 8).equals(solutionModel.getPosition()));
        Assert.assertEquals("7", solutionModel.getValue());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(8, related.size());
        Assert.assertTrue(new Position(0, 8).equals(related.get(0)));
        Assert.assertTrue(new Position(1, 8).equals(related.get(1)));
        Assert.assertTrue(new Position(2, 8).equals(related.get(2)));
        Assert.assertTrue(new Position(3, 8).equals(related.get(3)));
        Assert.assertTrue(new Position(4, 8).equals(related.get(4)));
        Assert.assertTrue(new Position(6, 8).equals(related.get(5)));
        Assert.assertTrue(new Position(7, 8).equals(related.get(6)));
        Assert.assertTrue(new Position(8, 8).equals(related.get(7)));
    }



    @Test
    public void box_hit_case_1() {
        String[][] board = new String[][] {
            {"7", "1", "8", "3", "4", "2", "", "", "5"},
            {"", "6", "9", "", "", "", "7", "", "2"},
            {"4", "2", "5", "", "", "", "", "", "3"},
            {"", "", "6", "4", "7", "3", "2", "", "1"},
            {"", "3", "", "", "", "", "", "1", "6"},
            {"", "", "2", "8", "5", "1", "6", "", ""},
            {"7", "", "", "", "", "", "", "", "8"},
            {"", "", "4", "", "", "", "1", "", ""},
            {"", "", "3", "6", "9", "7", "5", "", "4"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();


        Assert.assertTrue(new Position(1, 0).equals(solutionModel.getPosition()));
        Assert.assertEquals("3", solutionModel.getValue());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(8, related.size());
        Assert.assertTrue(new Position(0, 0).equals(related.get(0)));
        Assert.assertTrue(new Position(0, 1).equals(related.get(1)));
        Assert.assertTrue(new Position(0, 2).equals(related.get(2)));
        Assert.assertTrue(new Position(1, 1).equals(related.get(3)));
        Assert.assertTrue(new Position(1, 2).equals(related.get(4)));
        Assert.assertTrue(new Position(2, 0).equals(related.get(5)));
        Assert.assertTrue(new Position(2, 1).equals(related.get(6)));
        Assert.assertTrue(new Position(2, 2).equals(related.get(7)));
    }


    @Test
    public void box_hit_case_2() {
        String[][] board = new String[][] {
            {"7", "1", "8", "3", "4", "2", "", "", "5"},
            {"", "6", "", "", "", "", "7", "", "2"},
            {"4", "2", "5", "", "", "", "", "", "3"},
            {"", "", "6", "4", "7", "3", "2", "", "1"},
            {"", "3", "", "2", "", "9", "", "1", "6"},
            {"", "", "2", "8", "5", "1", "6", "", ""},
            {"7", "", "", "", "", "", "", "", "8"},
            {"", "", "4", "", "", "", "1", "", ""},
            {"", "", "3", "6", "9", "7", "5", "", "4"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(4, 4).equals(solutionModel.getPosition()));
        Assert.assertEquals("6", solutionModel.getValue());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(8, related.size());
        Assert.assertTrue(new Position(3, 3).equals(related.get(0)));
        Assert.assertTrue(new Position(3, 4).equals(related.get(1)));
        Assert.assertTrue(new Position(3, 5).equals(related.get(2)));
        Assert.assertTrue(new Position(4, 3).equals(related.get(3)));
        Assert.assertTrue(new Position(4, 5).equals(related.get(4)));
        Assert.assertTrue(new Position(5, 3).equals(related.get(5)));
        Assert.assertTrue(new Position(5, 4).equals(related.get(6)));
        Assert.assertTrue(new Position(5, 5).equals(related.get(7)));
    }


    @Test
    public void box_hit_case_3() {
        String[][] board = new String[][] {
            {"7", "1", "8", "3", "4", "2", "", "", "5"},
            {"", "6", "", "", "", "", "7", "", "2"},
            {"4", "2", "5", "", "", "", "", "", "3"},
            {"", "", "6", "4", "7", "3", "2", "", "1"},
            {"", "3", "", "2", "", "", "", "1", ""},
            {"", "", "2", "8", "5", "1", "6", "", ""},
            {"7", "", "", "", "", "", "", "6", "8"},
            {"", "", "4", "", "", "", "1", "3", "9"},
            {"", "", "3", "6", "9", "7", "5", "2", "4"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new LastFreeCellStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(6, 6).equals(solutionModel.getPosition()));
        Assert.assertEquals("7", solutionModel.getValue());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(8, related.size());
        Assert.assertTrue(new Position(6, 7).equals(related.get(0)));
        Assert.assertTrue(new Position(6, 8).equals(related.get(1)));
        Assert.assertTrue(new Position(7, 6).equals(related.get(2)));
        Assert.assertTrue(new Position(7, 7).equals(related.get(3)));
        Assert.assertTrue(new Position(7, 8).equals(related.get(4)));
        Assert.assertTrue(new Position(8, 6).equals(related.get(5)));
        Assert.assertTrue(new Position(8, 7).equals(related.get(6)));
        Assert.assertTrue(new Position(8, 8).equals(related.get(7)));
    }


}