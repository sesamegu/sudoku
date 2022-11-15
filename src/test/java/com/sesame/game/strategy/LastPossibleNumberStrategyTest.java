package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/14
 */
public class LastPossibleNumberStrategyTest {

    @Test
    public void by_number() {
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
        Optional<HintModel> result = new LastPossibleNumberStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(7, 7).equals(solutionModel.getPosition()));
        Assert.assertEquals("9", solutionModel.getSolutionDigital());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(20, related.size());
    }

    @Test
    public void by_candidate() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "1", ""},
            {"", "", "", "", "", "", "", "3", ""},
            {"", "", "", "", "", "", "", "4", ""},
            {"", "", "", "", "", "", "", "5", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "2", ""},
            {"", "", "", "", "", "6", "", "", ""},
            {"", "", "", "", "", "", "", "", "7"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        List<String> digital = new ArrayList<>();
        digital.add("9");
        puzzle.setCandidate(7, 7, digital);
        Optional<HintModel> result = new LastPossibleNumberStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(7, 7).equals(solutionModel.getPosition()));
        Assert.assertEquals("9", solutionModel.getSolutionDigital());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(20, related.size());
    }
}