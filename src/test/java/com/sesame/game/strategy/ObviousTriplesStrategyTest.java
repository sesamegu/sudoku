package com.sesame.game.strategy;

import java.util.List;
import java.util.Map;
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

        List<String> digitalString = candidateModel.getCauseDigital();
        Assert.assertEquals(3, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));
        Assert.assertTrue("7".equals(digitalString.get(2)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(2, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 5));
        Assert.assertEquals(3, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        Assert.assertEquals("7", delDigital.get(2));

        delDigital = deleteMap.get(new Position(0, 6));
        Assert.assertEquals(3, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        Assert.assertEquals("7", delDigital.get(2));

    }
}