package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/21
 */
public class HiddenTriplesStrategyTest {

    @Test
    public void row_test() {
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
        Optional<HintModel> result = new HiddenTriplesStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidateModel());
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

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.ROW, actual.getUnit());
        Assert.assertEquals(0, actual.getRow());

    }

    @Test
    public void row_test_not_the_case() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "6", "5", "", ""},
            {"4", "", "8", "", "5", "", "2", "", ""},
            {"5", "", "9", "2", "", "", "1", "", ""},
            {"", "", "1", "", "", "", "6", "9", "5"},
            {"3", "4", "5", "6", "9", "1", "8", "2", "7"},
            {"9", "7", "6", "5", "8", "2", "4", "1", "3"},
            {"1", "", "2", "", "6", "", "9", "4", "8"},
            {"", "", "", "", "1", "4", "", "", "2"},
            {"", "", "4", "", "2", "", "", "", "1"},
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);

        List<String> digital = new ArrayList<>();
        digital.add("2");
        digital.add("7");
        puzzle.setCandidate(0, 0, digital);

        digital = new ArrayList<>();
        digital.add("1");
        digital.add("2");
        digital.add("3");
        puzzle.setCandidate(0, 1, digital);

        digital = new ArrayList<>();
        digital.add("3");
        digital.add("7");
        puzzle.setCandidate(0, 2, digital);

        digital = new ArrayList<>();
        digital.add("1");
        digital.add("3");
        digital.add("4");
        digital.add("8");
        digital.add("9");
        puzzle.setCandidate(0, 3, digital);

        digital = new ArrayList<>();
        digital.add("3");
        digital.add("4");
        puzzle.setCandidate(0, 4, digital);

        digital = new ArrayList<>();
        digital.add("3");
        digital.add("8");
        puzzle.setCandidate(0, 7, digital);

        digital = new ArrayList<>();
        digital.add("4");
        digital.add("9");
        puzzle.setCandidate(0, 8, digital);

        Optional<HintModel> result = new HiddenTriplesStrategy().execute(puzzle);
        Assert.assertFalse(result.isPresent());

    }

}