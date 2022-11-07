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
 * @date 2022/10/24
 */
public class HiddenThreeStrategyTest {

    @Test
    public void row_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "7", "8", "6", "9"},
            {"4", "5", "", "", "", "", "", "3", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenThreeStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidateModel());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Assert.assertEquals(3, causeMap.size());
        List<String> digitalString = causeMap.get(new Position(0, 0));
        Assert.assertEquals(3, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));
        Assert.assertTrue("3".equals(digitalString.get(2)));

        digitalString = causeMap.get(new Position(0, 1));
        Assert.assertEquals(3, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));
        Assert.assertTrue("3".equals(digitalString.get(2)));

        digitalString = causeMap.get(new Position(0, 2));
        Assert.assertEquals(3, digitalString.size());
        Assert.assertTrue("1".equals(digitalString.get(0)));
        Assert.assertTrue("2".equals(digitalString.get(1)));
        Assert.assertTrue("3".equals(digitalString.get(2)));

        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();

        Assert.assertEquals(2, deleteMap.size());
        List<String> delDigital = deleteMap.get(new Position(0, 3));
        Assert.assertEquals(3, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        Assert.assertEquals("3", delDigital.get(2));

        delDigital = deleteMap.get(new Position(0, 4));
        Assert.assertEquals(3, delDigital.size());
        Assert.assertEquals("1", delDigital.get(0));
        Assert.assertEquals("2", delDigital.get(1));
        Assert.assertEquals("3", delDigital.get(2));

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.ROW, actual.getUnit());
        Assert.assertEquals(0, actual.getRow());
    }

}