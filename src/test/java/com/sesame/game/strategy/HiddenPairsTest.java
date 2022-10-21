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
 * @date 2022/10/19
 */
public class HiddenPairsTest {

    @Test
    public void box_test() {
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
        Optional<HintModel> result = new HiddenPairs().tryStrategy(puzzle);
        Assert.assertTrue(result.isPresent());
        HintModel hintModel = result.get();
        Assert.assertTrue(hintModel.isCandidate());
        CandidateModel candidateModel = hintModel.getCandidateModel();

        List<Position> causeList = candidateModel.getCauseList();
        Assert.assertEquals(2, causeList.size());
        Assert.assertTrue(new Position(4, 0).equals(causeList.get(0)));
        Assert.assertTrue(new Position(5, 0).equals(causeList.get(1)));

        List<String> digitalString = candidateModel.getCauseDigital();
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

    }

}