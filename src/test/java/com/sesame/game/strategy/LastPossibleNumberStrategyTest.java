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
 * @date 2022/10/14
 */
public class LastPossibleNumberStrategyTest {

    @Test
    public void tryStrategy() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "1", ""},
            {"", "", "", "", "", "", "", "3", ""},
            {"", "", "", "", "", "", "", "4", ""},
            {"", "", "", "", "", "", "", "5", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "2", ""},
            {"", "", "", "", "8", "6", "", "", ""},
            {"", "", "", "", "", "", "", "", "7"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new LastPossibleNumberStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(7, 7).equals(solutionModel.getPosition()));
        Assert.assertEquals("9", solutionModel.getValue());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(20, related.size());
    }
}