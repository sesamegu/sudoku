package com.sesame.game.strategy;

import java.util.Optional;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;

/**
 * Introduction: the strategy interface
 *
 * @author sesame 2022/10/13
 */
public interface FillStrategy {
    /**
     * execute the strategy
     *
     * @param sudokuPuzzle
     * @return
     */
    Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle);

    /**
     * the strategy's  name
     *
     * @return
     */
    Strategy getStrategy();

    /**
     * the strategy desc
     *
     * @return
     */
    String buildDesc(HintModel hintModel);
}
