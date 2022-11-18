package com.sesame.game.strategy;

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
 * @date 2022/11/18
 */
public class RowColumnToBoxTest {

    @Test
    public void box_row_test() {
        String[][] board = new String[][] {
            {"1", "2", "", "3", "", "4", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "5", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "5", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new RowColumnToBox().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidateModel());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(0, 6)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 7)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 8)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(1, digitalString.size());
        Assert.assertTrue("5".equals(digitalString.get(0)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(6, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(1, 6));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 7));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(1, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(2, 6));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(2, 7));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(2, 8));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(2, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.ROW, actual.getUnit());
        Assert.assertEquals(0, actual.getRow());

        actual = unitModelList.get(1);
        Assert.assertEquals(Unit.BOX, actual.getUnit());
        Assert.assertEquals(0, actual.getRow());
        Assert.assertEquals(6, actual.getColumn());

    }

    @Test
    public void box_column_test() {
        String[][] board = new String[][] {
            {"1", "", "", "", "", "", "", "", ""},
            {"2", "", "", "", "", "", "", "", ""},
            {"4", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "5", "", ""},
            {"3", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "5", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},

        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new RowColumnToBox().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidateModel());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(6, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(7, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(8, 0)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(1, digitalString.size());
        Assert.assertTrue("5".equals(digitalString.get(0)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(6, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(6, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(7, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 1));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(6, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(7, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        delDigital = deleteMap.get(new Position(8, 2));
        Assert.assertEquals(1, delDigital.size());
        Assert.assertEquals("5", delDigital.get(0));

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(2, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.COLUMN, actual.getUnit());
        Assert.assertEquals(0, actual.getColumn());

        actual = unitModelList.get(1);
        Assert.assertEquals(Unit.BOX, actual.getUnit());
        Assert.assertEquals(6, actual.getRow());
        Assert.assertEquals(0, actual.getColumn());

    }

}