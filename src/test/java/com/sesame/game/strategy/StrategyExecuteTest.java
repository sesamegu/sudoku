package com.sesame.game.strategy;

import java.util.Optional;

import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.library.PuzzleLibrary;
import com.sesame.game.strategy.model.HintModel;
import org.junit.Test;
import org.springframework.util.Assert;

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
        for (int i = 1; i <= 10; i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.EASY, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.isTrue(hintModel.isPresent(), "should be solved");
        }

        //normal
        for (int i = 1; i <= 10; i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.NORMAL, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.isTrue(hintModel.isPresent(), "should be solved");
        }

        //hard
        for (int i = 1; i <= 10; i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.HARD, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.isTrue(hintModel.isPresent(), "should be solved");
        }

        //vip
        for (int i = 1; i <= 5; i++) {
            SudokuPuzzle aCase = PuzzleLibrary.getCase(GameLevel.VIP, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase);
            Assert.isTrue(hintModel.isPresent(), "should be solved");
        }

    }

}