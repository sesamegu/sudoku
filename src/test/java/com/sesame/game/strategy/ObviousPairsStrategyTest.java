package com.sesame.game.strategy;

import java.util.List;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mike
 * @date 2022/10/17
 */
public class ObviousPairsStrategyTest {

    @Test
    public void row_test() {

        String[][] board = new String[][] {
            {"", "", "3", "4", "5", "6", "", "8", "7"},
            {"4", "5", "6", "", "", "", "", "", ""},
            {"7", "9", "8", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new ObviousPairsStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        List<Position> causeList = candidateModel.getCauseList();
        Assert.assertEquals(2, causeList.size());
        Assert.assertTrue(new Position(0, 0).equals(causeList.get(0)));
        Assert.assertTrue(new Position(0, 1).equals(causeList.get(1)));

        List<String> digitalString = candidateModel.getDigitalString();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));

        List<Position> relatedList = candidateModel.getRelatedList();
        Assert.assertEquals(1, relatedList.size());
        Assert.assertTrue(new Position(0, 6).equals(relatedList.get(0)));

    }


    @Test
    public void column_test() {

        String[][] board = new String[][] {
            {"7", "8", "6", "9", "", "3", "4", "5", ""},
            {"3", "4", "5", "6", "", "7", "8", "9", ""},
            {"", "", "", "", "3", "", "", "", ""},
            {"", "", "", "", "4", "", "", "", ""},
            {"", "", "", "", "5", "", "", "", ""},
            {"", "", "", "", "6", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "7", "", "", "", ""},
            {"", "", "", "", "9", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new ObviousPairsStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        List<Position> causeList = candidateModel.getCauseList();
        Assert.assertEquals(2, causeList.size());
        Assert.assertTrue(new Position(0, 4).equals(causeList.get(0)));
        Assert.assertTrue(new Position(1, 4).equals(causeList.get(1)));

        List<String> digitalString = candidateModel.getDigitalString();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));

        List<Position> relatedList = candidateModel.getRelatedList();
        Assert.assertEquals(1, relatedList.size());
        Assert.assertTrue(new Position(6, 4).equals(relatedList.get(0)));

    }

    @Test
    public void box_test() {

        String[][] board = new String[][] {
            {"1", "2", "", "", "", "7", "", "", ""},
            {"4", "3", "6", "", "", "", "", "", ""},
            {"", "", "5", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "7", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new ObviousPairsStrategy().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        List<Position> causeList = candidateModel.getCauseList();
        Assert.assertEquals(2, causeList.size());
        Assert.assertTrue(new Position(0, 2).equals(causeList.get(0)));
        Assert.assertTrue(new Position(2, 1).equals(causeList.get(1)));

        List<String> digitalString = candidateModel.getDigitalString();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("8".equals(digitalString.get(0)));
        Assert.assertTrue("9".equals(digitalString.get(1)));

        List<Position> relatedList = candidateModel.getRelatedList();
        Assert.assertEquals(1, relatedList.size());
        Assert.assertTrue(new Position(2, 0).equals(relatedList.get(0)));

    }
}