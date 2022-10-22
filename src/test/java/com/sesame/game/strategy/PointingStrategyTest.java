package com.sesame.game.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mike
 * @date 2022/10/22
 */
public class PointingStrategyTest {

    @Test
    public void box_row_test() {

        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "3", "", "", "", "", ""},
            {"8", "7", "6", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new PointingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(0, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 1)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 2)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(1, digitalString.size());
        Assert.assertTrue("3".equals(digitalString.get(0)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(3, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 6));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));

        delDigital = deleteMap.get(new Position(0, 7));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));

        delDigital = deleteMap.get(new Position(0, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));


    }



    @Test
    public void box_column_test() {

        String[][] board = new String[][] {
            {"", "", "", "", "", "", "3", "", ""},
            {"7", "", "", "", "", "", "", "", ""},
            {"8", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "3", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new PointingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(2, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(1, 2)));
        Assert.assertTrue(causeMap.containsKey(new Position(2, 2)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(1, digitalString.size());
        Assert.assertTrue("3".equals(digitalString.get(0)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(3, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(6, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));

        delDigital = deleteMap.get(new Position(7, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));


    }
}