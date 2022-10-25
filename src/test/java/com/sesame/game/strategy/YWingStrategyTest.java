package com.sesame.game.strategy;

import java.util.ArrayList;
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

    @Test
    public void test_box_right_up_first() {
        String[][] board = new String[][] {
            {"8", "9", "1", "3", "", "", "", "", "6"},
            {"3", "", "7", "6", "2", "9", "", "8", "1"},
            {"6", "", "2", "", "8", "1", "", "", "7"},
            {"4", "7", "", "", "6", "", "", "1", "2"},
            {"5", "3", "", "", "1", "2", "", "6", "4"},
            {"1", "2", "6", "8", "", "4", "", "", "9"},
            {"9", "1", "5", "2", "", "", "6", "", "8"},
            {"7", "8", "3", "", "9", "6", "", "", "5"},
            {"2", "6", "4", "", "5", "8", "", "", "3"}
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
        List<String> strings = causeMap.get(new Position(3, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("5", strings.get(1));

        strings = causeMap.get(new Position(5, 4));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("7", strings.get(1));

        strings = causeMap.get(new Position(0, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("5", strings.get(0));
        Assert.assertEquals("7", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("7", delDigital.get(0));
    }

    @Test
    public void test_box_right_down_first() {
        String[][] board = new String[][] {
            {"9", "1", "5", "2", "", "", "6", "", "8"},
            {"7", "8", "3", "", "9", "6", "", "", "5"},
            {"2", "6", "4", "", "5", "8", "", "", "3"},
            {"4", "7", "", "", "6", "", "", "1", "2"},
            {"5", "3", "", "", "1", "2", "", "6", "4"},
            {"1", "2", "6", "8", "", "4", "", "", "9"},
            {"8", "9", "1", "3", "", "", "", "", "6"},
            {"3", "", "7", "6", "2", "9", "", "8", "1"},
            {"6", "", "2", "", "8", "1", "", "", "7"},
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
        List<String> strings = causeMap.get(new Position(3, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("5", strings.get(1));

        strings = causeMap.get(new Position(5, 4));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("3", strings.get(0));
        Assert.assertEquals("7", strings.get(1));

        strings = causeMap.get(new Position(6, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("5", strings.get(0));
        Assert.assertEquals("7", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(6, 4));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("7", delDigital.get(0));
    }

    @Test
    public void test_box_left_up_first() {
        String[][] board = new String[][] {
            {"1", "8", "4", "", "", "3", "5", "6", "9"},
            {"7", "3", "", "", "", "5", "", "8", "1"},
            {"", "6", "5", "1", "8", "", "", "7", "3"},
            {"3", "4", "7", "8", "5", "1", "9", "2", "6"},
            {"6", "5", "8", "", "", "", "3", "1", "4"},
            {"", "1", "", "3", "", "", "7", "5", "8"},
            {"5", "2", "1", "9", "3", "8", "6", "4", "7"},
            {"8", "9", "6", "", "", "7", "1", "3", ""},
            {"4", "7", "3", "", "1", "", "8", "9", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        List<String> digital = new ArrayList<>();
        digital.add("4");
        digital.add("6");

        puzzle.setCandidate(5, 4, digital);
        puzzle.setCandidate(5, 5, new ArrayList<>(digital));

        Optional<HintModel> result = new YWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> strings = causeMap.get(new Position(7, 4));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("2", strings.get(0));
        Assert.assertEquals("4", strings.get(1));

        strings = causeMap.get(new Position(8, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("2", strings.get(0));
        Assert.assertEquals("6", strings.get(1));

        strings = causeMap.get(new Position(5, 4));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("4", strings.get(0));
        Assert.assertEquals("6", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(5, 5));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));
    }

    @Test
    public void test_box_left_down_first() {
        String[][] board = new String[][] {
            {"5", "2", "1", "9", "3", "8", "6", "4", "7"},
            {"8", "9", "6", "", "", "7", "1", "3", ""},
            {"4", "7", "3", "", "1", "", "8", "9", ""},
            {"3", "4", "7", "8", "5", "1", "9", "2", "6"},
            {"6", "5", "8", "", "", "", "3", "1", "4"},
            {"", "1", "", "3", "", "", "7", "5", "8"},
            {"1", "8", "4", "", "", "3", "5", "6", "9"},
            {"7", "3", "", "", "", "5", "", "8", "1"},
            {"", "6", "5", "1", "8", "", "", "7", "3"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        List<String> digital = new ArrayList<>();
        digital.add("4");
        digital.add("6");

        puzzle.setCandidate(5, 4, digital);
        puzzle.setCandidate(5, 5, new ArrayList<>(digital));

        Optional<HintModel> result = new YWingStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> strings = causeMap.get(new Position(5, 4));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("4", strings.get(0));
        Assert.assertEquals("6", strings.get(1));

        strings = causeMap.get(new Position(2, 5));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("2", strings.get(0));
        Assert.assertEquals("6", strings.get(1));

        strings = causeMap.get(new Position(1, 4));
        Assert.assertEquals(2, strings.size());
        Assert.assertEquals("2", strings.get(0));
        Assert.assertEquals("4", strings.get(1));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(5, 5));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("6", delDigital.get(0));
    }

    @Test
    public void test_box_left_up_second() {
        //todo 测试box 第二列
        //todo 测试box 横向
    }

}

