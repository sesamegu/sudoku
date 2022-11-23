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
 * @date 2022/10/19
 */
public class HiddenPairsStrategyTest {

    @Test
    public void column_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "2", "6", "", "", "", "", "", ""},
            {"", "1", "", "", "2", "", "6", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "5", "", "", "", "", "", "", ""},
            {"", "6", "2", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenPairsStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidateModel());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(2, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(4, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(5, 0)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(2, digitalString.size());
        Assert.assertTrue("2".equals(digitalString.get(0)));
        Assert.assertTrue("6".equals(digitalString.get(1)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();

        Assert.assertEquals(2, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(4, 0));
        Assert.assertEquals(5, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));
        Assert.assertEquals("4", delDigital.get(1));
        Assert.assertEquals("7", delDigital.get(2));
        Assert.assertEquals("8", delDigital.get(3));
        Assert.assertEquals("9", delDigital.get(4));

        delDigital = deleteMap.get(new Position(5, 0));
        Assert.assertEquals(5, delDigital.size());
        Assert.assertEquals("3", delDigital.get(0));
        Assert.assertEquals("4", delDigital.get(1));
        Assert.assertEquals("7", delDigital.get(2));
        Assert.assertEquals("8", delDigital.get(3));
        Assert.assertEquals("9", delDigital.get(4));

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.COLUMN, actual.getUnit());
        Assert.assertEquals(0, actual.getColumn());

    }

}