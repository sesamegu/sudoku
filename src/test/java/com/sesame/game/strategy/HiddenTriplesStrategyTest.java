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
 * @date 2022/10/21
 */
public class HiddenTriplesStrategyTest {

    @Test
    public void box_test() {
        String[][] board = new String[][] {
            {"", "4", "", "", "5", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "1"},
            {"", "", "3", "", "", "", "", "", "2"},
            {"", "", "", "2", "", "3", "", "", ""},
            {"", "", "", "", "", "", "", "3", ""},
            {"3", "", "1", "", "", "2", "", "", ""},
            {"", "", "", "", "", "1", "", "2", "3"},
            {"", "", "2", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenTriplesStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(0, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 3)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 6)));

        List<String> digitalString = causeMap.get(new Position(0, 0));
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));

        digitalString = causeMap.get(new Position(0, 3));
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("3".equals(digitalString.get(1)));

        digitalString = causeMap.get(new Position(0, 6));
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("2".equals(digitalString.get(0)));
        Assert.assertTrue("3".equals(digitalString.get(1)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();

        Assert.assertEquals(3, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 0));
        Assert.assertEquals(4, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));
        Assert.assertEquals("7", delDigital.get(1));
        Assert.assertEquals("8", delDigital.get(2));
        Assert.assertEquals("9", delDigital.get(3));

        delDigital = deleteMap.get(new Position(0, 3));
        Assert.assertEquals(4, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));
        Assert.assertEquals("7", delDigital.get(1));
        Assert.assertEquals("8", delDigital.get(2));
        Assert.assertEquals("9", delDigital.get(3));

        delDigital = deleteMap.get(new Position(0, 6));
        Assert.assertEquals(4, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));
        Assert.assertEquals("7", delDigital.get(1));
        Assert.assertEquals("8", delDigital.get(2));
        Assert.assertEquals("9", delDigital.get(3));

    }

}