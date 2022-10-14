package com.sesame.game.strategy;

import java.util.Optional;

import com.sesame.game.SudokuPuzzle;

/**
 * Introduction: 策略接口
 *
 * @author sesame 2022/10/13
 */
public interface FillStrategy {
    /**
     * 执行策略
     *
     * @param sudokuPuzzle
     * @return
     */
    Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle);

    /**
     * 策略名称
     *
     * @return
     */
    Strategy getStrategy();

    /**
     * 策略优先级
     *
     * @return
     */
    int priority();
}
