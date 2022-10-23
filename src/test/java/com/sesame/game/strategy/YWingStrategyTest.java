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
public class YWingStrategyTest {

    @Test
    public void test_right_down() {
        String[][] board = new String[][] {
            {"9", "", "", "2", "", "", "7", "5", ""},
            {"", "5", "", "", "", "", "", "", ""},
            {"4", "2", "", "", "", "", "", "", ""},
            {"", "9", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "7", "", "", "", "6", "", "", ""},
            {"", "6", "", "", "", "1", "", "", ""},
            {"", "1", "", "", "", "3", "", "", ""},
            {"", "", "", "", "8", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new YWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> strings = causeMap.get(new Position(0, 1));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        strings = causeMap.get(new Position(0, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("4", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        strings = causeMap.get(new Position(8, 1));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("4", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(8, 5));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));
    }

    @Test
    public void test_left_down() {
        String[][] board = new String[][] {
            {"9", "", "", "2", "", "", "7", "5", ""},
            {"1", "5", "", "", "3", "", "", "", ""},
            {"4", "2", "", "", "", "", "", "", ""},
            {"", "9", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "7", "", "", "", "6", "", "", ""},
            {"", "6", "", "5", "", "", "", "", ""},
            {"", "", "", "8", "7", "1", "", "", ""},
            {"", "", "", "6", "9", "", "", "2", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new YWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> strings = causeMap.get(new Position(0, 1));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        strings = causeMap.get(new Position(0, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("4", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        strings = causeMap.get(new Position(8, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("4", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(8, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));
    }

    @Test
    public void test_left_up() {
        String[][] board = new String[][] {
            {"", "", "", "2", "", "", "7", "5", "9"},
            {"", "5", "", "", "3", "", "", "", ""},
            {"", "2", "", "", "", "", "", "", ""},
            {"", "9", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "7", "", "", "", "6", "", "", ""},
            {"9", "6", "1", "5", "", "", "", "", ""},
            {"4", "", "", "8", "7", "1", "", "", ""},
            {"", "", "", "6", "9", "", "", "2", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new YWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> strings = causeMap.get(new Position(0, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("4", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        strings = causeMap.get(new Position(8, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("4", strings.get(1));

        strings = causeMap.get(new Position(8, 1));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("8", delDigital.get(0));
    }

    @Test
    public void test_right_up() {
        String[][] board = new String[][] {
            {"", "", "", "2", "", "", "7", "5", "9"},
            {"3", "5", "", "", "", "", "", "", ""},
            {"1", "2", "", "", "", "", "", "", ""},
            {"", "9", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "7", "", "", "", "6", "", "", ""},
            {"9", "6", "1", "5", "", "", "", "", ""},
            {"4", "", "", "8", "7", "1", "", "", ""},
            {"", "", "", "6", "9", "", "", "2", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new YWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> strings = causeMap.get(new Position(8, 1));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        strings = causeMap.get(new Position(8, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("4", strings.get(1));

        strings = causeMap.get(new Position(0, 1));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("4", strings.get(0));
        Assert.assertEquals("8", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 5));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("4", delDigital.get(0));
    }
}

