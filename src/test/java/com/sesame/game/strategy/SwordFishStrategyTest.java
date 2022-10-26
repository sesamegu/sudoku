package com.sesame.game.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/24
 */
public class SwordFishStrategyTest {

    @Test
    public void test_row() {
        String[][] board = new String[][] {
            {"9", "", "8", "7", "3", "5", "1", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "6", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "4", "3", "2", "5", "", "9", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "6", ""},
            {"", "", "1", "8", "4", "7", "", "", "9"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new SwordFishStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(6, causeMap.size());
        List<String> strings = causeMap.get(new Position(0, 1));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(0, 8));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(5, 0));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(5, 8));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(8, 0));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(8, 1));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(11, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(1, 0));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(2, 0));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(4, 0));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(6, 0));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(2, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(4, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(6, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(2, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(4, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

    }

    @Test
    public void test_column() {
        String[][] board = new String[][] {
            {"9", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "4", "", "", ""},
            {"8", "", "", "", "", "3", "", "", "1"},
            {"7", "", "", "", "", "2", "", "", "8"},
            {"3", "", "", "6", "", "5", "", "", "4"},
            {"5", "", "", "", "", "", "", "", "7"},
            {"1", "", "", "", "", "9", "", "", ""},
            {"", "", "", "", "", "", "", "6", ""},
            {"", "", "", "", "", "", "", "", "9"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new SwordFishStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(6, causeMap.size());
        List<String> strings = causeMap.get(new Position(1, 0));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(8, 0));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(0, 5));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(8, 5));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(0, 8));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        strings = causeMap.get(new Position(1, 8));
        Assert.assertEquals(1, strings.size());
        Assert.assertEquals("6", strings.get(0));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(11, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(0, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(0, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(0, 6));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 6));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));

    }
}