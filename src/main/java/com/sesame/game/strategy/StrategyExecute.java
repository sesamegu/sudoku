package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;

/**
 * Introduction: execute the all strategy in the order
 *
 * @author sesame 2022/10/26
 */
public abstract class StrategyExecute {
    private static final List<FillStrategy> allStrategy = new ArrayList<>();

    static {
        allStrategy.add(new LastFreeCellStrategy());
        allStrategy.add(new LastPossibleNumberStrategy());
        allStrategy.add(new HiddenSinglesStrategy());
        allStrategy.add(new ObviousPairsStrategy());
        allStrategy.add(new ObviousTriplesStrategy());
        allStrategy.add(new HiddenPairsStrategy());
        allStrategy.add(new HiddenTriplesStrategy());
        allStrategy.add(new HiddenThreeStrategy());
        allStrategy.add(new PointingStrategy());
        allStrategy.add(new XWingStrategy());
        allStrategy.add(new YWingStrategy());
        allStrategy.add(new SwordFishStrategy());
    }

    public static Optional<HintModel> tryAllStrategy(SudokuPuzzle puzzle) {
        for (FillStrategy one : allStrategy) {
            Optional<HintModel> hintModel = one.execute(puzzle);
            if (hintModel.isPresent()) {
                return hintModel;
            }
        }

        return Optional.empty();
    }

}
