package com.sesame.game.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import org.junit.Assert;
import org.junit.Test;

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
        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(2, causeMap.size());

        Assert.assertTrue(causeMap.containsKey(new Position(0, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 1)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();

        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 6));
        Assert.assertEquals(2, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));


        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.ROW, actual.getUnit());
        Assert.assertEquals(0, actual.getRow());


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

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(2, causeMap.size());

        Assert.assertTrue(causeMap.containsKey(new Position(0, 4)));
        Assert.assertTrue(causeMap.containsKey(new Position(1, 4)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();

        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(6, 4));
        Assert.assertEquals(2, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.COLUMN, actual.getUnit());
        Assert.assertEquals(4, actual.getColumn());
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

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(2, causeMap.size());

        Assert.assertTrue(causeMap.containsKey(new Position(0, 2)));
        Assert.assertTrue(causeMap.containsKey(new Position(2, 1)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("8".equals(digitalString.get(0)));
        Assert.assertTrue("9".equals(digitalString.get(1)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();

        Assert.assertEquals(1, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(2, 0));
        Assert.assertEquals(2, delDigital.size());
        Assert.assertEquals("8", delDigital.get(0));
        Assert.assertEquals("9", delDigital.get(1));


        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.BOX, actual.getUnit());
        Assert.assertEquals(0, actual.getColumn());
        Assert.assertEquals(0, actual.getColumn());

    }
}