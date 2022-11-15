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
public class ObviousTriplesStrategyTest {

    @Test
    public void row_test() {

        String[][] board = new String[][] {
            {"", "", "3", "4", "5", "", "", "8", ""},
            {"4", "5", "6", "", "", "", "", "", ""},
            {"", "9", "8", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "2"},
            {"", "", "", "", "", "", "", "", ""},
            {"7", "", "", "", "", "", "", "", "6"},
            {"", "1", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "9"}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new ObviousTriplesStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidateModel());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        Assert.assertTrue(causeMap.containsKey(new Position(0, 0)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 1)));
        Assert.assertTrue(causeMap.containsKey(new Position(0, 8)));

        List<String> digitalString = causeMap.values().iterator().next();
        Assert.assertEquals(3, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));
        Assert.assertTrue("7".equals(digitalString.get(2)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        Assert.assertEquals(2, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 5));
        Assert.assertEquals(3, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        Assert.assertEquals("7", delDigital.get(2));

        delDigital = deleteMap.get(new Position(0, 6));
        Assert.assertEquals(3, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        Assert.assertEquals("7", delDigital.get(2));

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.ROW, actual.getUnit());
        Assert.assertEquals(0, actual.getRow());

    }
}