package com.sesame.game.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mike
 * @date 2022/10/15
 */
public class HiddenSinglesStrategyTest {

    @Test
    public void row_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "8", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "4", "5", "6", "7"},
            {"", "", "", "", "", "", "", "", ""},
            {"", "8", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenSinglesStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(6, 3).equals(solutionModel.getPosition()));
        Assert.assertEquals("8", solutionModel.getSolutionDigital());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(10, related.size());

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.ROW, actual.getUnit());
        Assert.assertEquals(6, actual.getRow());
    }

    @Test
    public void column_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "5", "", "", "", ""},
            {"", "", "", "", "4", "", "", "", ""},
            {"", "", "", "", "6", "", "", "", ""},
            {"", "", "", "", "7", "", "", "", ""},
            {"", "8", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "8", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenSinglesStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(5, 4).equals(solutionModel.getPosition()));
        Assert.assertEquals("8", solutionModel.getSolutionDigital());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(10, related.size());

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.COLUMN, actual.getUnit());
        Assert.assertEquals(4, actual.getColumn());
    }

    @Test
    public void box_test() {
        String[][] board = new String[][] {
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "8", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "8", "", ""},
            {"", "1", "", "", "", "", "", "", ""},
            {"9", "6", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(board);
        Optional<HintModel> result = new HiddenSinglesStrategy().execute(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        SolutionModel solutionModel = hintModel.getSolutionModel();

        Assert.assertTrue(new Position(7, 0).equals(solutionModel.getPosition()));
        Assert.assertEquals("8", solutionModel.getSolutionDigital());
        List<Position> related = solutionModel.getRelated();
        Assert.assertEquals(10, related.size());

        List<UnitModel> unitModelList = hintModel.getUnitModelList();
        Assert.assertEquals(1, unitModelList.size());
        UnitModel actual = unitModelList.get(0);
        Assert.assertEquals(Unit.BOX, actual.getUnit());
        Assert.assertEquals(6, actual.getRow());
        Assert.assertEquals(0, actual.getColumn());

    }

    @Test
    public void testMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 1);
        map.put("3", 1);

        // 检查是否存在 某个数只有一个
        List<String> collect = map.entrySet().stream().filter(one -> one.getValue() == 1)
            .map(one -> one.getKey()).collect(Collectors.toList());

        //默认数字序
        Collections.sort(collect);
        Assert.assertEquals("1", collect.get(0));

    }
}