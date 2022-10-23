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
 * @date 2022/10/23
 */
public class XWingStrategyTest {

    @Test
    public void row_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"1", "", "", "3", "", "5", "7", "2", "8"},
            {"", "", "", "", "", "", "", "", ""},
            {"8", "", "1", "9", "", "6", "2", "5", "7"},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "4", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new XWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(4, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(2, 1)));
        Assert.assertTrue(causeMap.containsKey(new Position(4, 1)));
        Assert.assertTrue(causeMap.containsKey(new Position(2, 4)));
        Assert.assertTrue(causeMap.containsKey(new Position(4, 4)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(1, digitalString.size());
        Assert.assertTrue("4".equals(digitalString.get(0)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(10, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(3, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(5, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(0, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(3, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(5, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(7, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));
    }

    @Test
    public void column_test() {
        String[][] board = new String[][] {
            {"", "", "", "1", "", "", "2", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "3", "", "", "7", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"4", "", "", "7", "", "", "", "", ""},
            {"", "", "", "2", "", "", "6", "", ""},
            {"", "", "", "6", "", "", "8", "", ""},
            {"", "", "", "8", "", "", "9", "", ""},
            {"", "", "", "9", "", "", "3", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new XWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(4, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(1, 3)));
        Assert.assertTrue(causeMap.containsKey(new Position(1, 6)));
        Assert.assertTrue(causeMap.containsKey(new Position(3, 3)));
        Assert.assertTrue(causeMap.containsKey(new Position(3, 6)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(1, digitalString.size());
        Assert.assertTrue("4".equals(digitalString.get(0)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(10, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(1, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 5));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 7));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(3, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(3, 5));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(3, 7));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));

        delDigital = deleteMap.get(new Position(3, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));
    }

}