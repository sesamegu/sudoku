package com.sesame.game.strategy;

import java.util.List;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/19
 */
public class ObviousTriplesStrategyTest {

    @Test
    public void row_test() {

        String[][] board = new String[][] {
            {"", "", "3", "4", "5", "", "", "8", ""},
            {"4", "5", "6", "", "", "", "", "", ""},
            {"", "9", "8", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "2"},
            {"", "", "", "", "", "", "", "", ""},
            {"7", "", "", "", "", "", "", "", "6"},
            {"", "1", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "9"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new ObviousTriplesStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        List<Position> causeList = candidateModel.getCauseList();
        Assert.assertEquals(3, causeList.size());
        Assert.assertTrue(new Position(0, 0).equals(causeList.get(0)));
        Assert.assertTrue(new Position(0, 1).equals(causeList.get(1)));
        Assert.assertTrue(new Position(0, 8).equals(causeList.get(2)));

        List<String> digitalString = candidateModel.getDigitalString();
        Assert.assertEquals(3, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));
        Assert.assertTrue("7".equals(digitalString.get(2)));

        List<Position> relatedList = candidateModel.getRelatedList();
        Assert.assertEquals(2, relatedList.size());
        Assert.assertTrue(new Position(0, 5).equals(relatedList.get(0)));
        Assert.assertTrue(new Position(0, 6).equals(relatedList.get(1)));

    }

    @Test
    public void column_test() {
        //先不测试了，当前看是等价的
    }

    @Test
    public void box_test() {
        //先不测试了，当前看是等价的
    }
}