package com.sesame.game.strategy;

import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.service.LevelService;
import com.sesame.game.strategy.model.HintModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author sesame 2022/10/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyExecuteTest {
    @Autowired
    private LevelService levelService;

    @Test
    public void testEasy() {
        for (int i = 1; i <= 2; i++) {
            SudokuPuzzle aCase = levelService.getPuzzle(GameLevel.EASY, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase, I18nProcessor.getLocale("en_US"));
            Assert.assertTrue(hintModel.isPresent());
        }
    }

    @Test
    public void testNormal() {
        for (int i = 1; i <= 2; i++) {
            SudokuPuzzle aCase = levelService.getPuzzle(GameLevel.NORMAL, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase, I18nProcessor.getLocale("en_US"));
            Assert.assertTrue(hintModel.isPresent());
        }
    }

    @Test
    public void testHard() {
        for (int i = 1; i <= 2; i++) {
            SudokuPuzzle aCase = levelService.getPuzzle(GameLevel.HARD, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase, I18nProcessor.getLocale("en_US"));
            Assert.assertTrue(hintModel.isPresent());
        }
    }

    @Test
    public void testVip() {
        for (int i = 1; i <= 2; i++) {
            SudokuPuzzle aCase = levelService.getPuzzle(GameLevel.VIP, i);
            Optional<HintModel> hintModel = StrategyExecute.tryAllStrategy(aCase, I18nProcessor.getLocale("en_US"));
            Assert.assertTrue(hintModel.isPresent());
        }
    }

}