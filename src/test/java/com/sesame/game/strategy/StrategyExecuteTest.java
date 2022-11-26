package com.sesame.game.strategy;

import java.util.Optional;

import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.library.FileLibrary;
import com.sesame.game.library.PuzzleLibrary;
import com.sesame.game.strategy.model.HintModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * run the library and make sure all puzzles are solved
 *
 * @author mike
 * @date 2022/11/21
 */
public class StrategyExecuteTest {

    @Test
    public void runTheLibrary() {
        // easy
        for (int i = 1; i <= FileLibrary.size(GameLevel.EASY); i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.EASY, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.assertTrue("should be solved", hintModel.isPresent());
        }

        //normal
        for (int i = 1; i <= FileLibrary.size(GameLevel.NORMAL); i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.NORMAL, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.assertTrue("should be solved", hintModel.isPresent());
        }

        //hard
        for (int i = 1; i <= FileLibrary.size(GameLevel.HARD); i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.HARD, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.assertTrue("should be solved", hintModel.isPresent());
        }

        //vip
        for (int i = 1; i <= FileLibrary.size(GameLevel.VIP); i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.VIP, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.assertTrue("should be solved", hintModel.isPresent());
        }

    }

}