package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.HintModel;
import org.springframework.util.Assert;

/**
 * Introduction: execute the all strategy in the order
 *
 * @author sesame 2022/10/26
 */
public abstract class StrategyExecute {
    private static final List<FillStrategy> allStrategy = new ArrayList<>();
    private static final Map<Strategy, FillStrategy> strategyMap = new HashMap<>();

    static {
        LastFreeCellStrategy lastFreeCellStrategy = new LastFreeCellStrategy();
        allStrategy.add(lastFreeCellStrategy);
        strategyMap.put(Strategy.LAST_FREE_CELL, lastFreeCellStrategy);

        LastPossibleNumberStrategy lastPossibleNumberStrategy = new LastPossibleNumberStrategy();
        allStrategy.add(lastPossibleNumberStrategy);
        strategyMap.put(Strategy.LAST_POSSIBLE_NUMBER, lastPossibleNumberStrategy);

        HiddenSinglesStrategy hiddenSinglesStrategy = new HiddenSinglesStrategy();
        allStrategy.add(hiddenSinglesStrategy);
        strategyMap.put(Strategy.HIDDEN_SINGLES, hiddenSinglesStrategy);

        ObviousPairsStrategy obviousPairsStrategy = new ObviousPairsStrategy();
        allStrategy.add(obviousPairsStrategy);
        strategyMap.put(Strategy.OBVIOUS_PAIRS, obviousPairsStrategy);

        ObviousTriplesStrategy obviousTriplesStrategy = new ObviousTriplesStrategy();
        allStrategy.add(obviousTriplesStrategy);
        strategyMap.put(Strategy.OBVIOUS_TRIPLES, obviousTriplesStrategy);

        HiddenPairsStrategy hiddenPairsStrategy = new HiddenPairsStrategy();
        allStrategy.add(hiddenPairsStrategy);
        strategyMap.put(Strategy.HIDDEN_PAIRS, hiddenPairsStrategy);

        HiddenTriplesStrategy hiddenTriplesStrategy = new HiddenTriplesStrategy();
        allStrategy.add(hiddenTriplesStrategy);
        strategyMap.put(Strategy.HIDDEN_TRIPLES, hiddenTriplesStrategy);

        PointingStrategy pointingStrategy = new PointingStrategy();
        allStrategy.add(pointingStrategy);
        strategyMap.put(Strategy.POINTING_PAIRS, pointingStrategy);

        RowColumnToBox rowColumnToBox = new RowColumnToBox();
        allStrategy.add(rowColumnToBox);
        strategyMap.put(Strategy.ROW_COLUMN_TO_BOX, rowColumnToBox);

        XWingStrategy xWingStrategy = new XWingStrategy();
        allStrategy.add(xWingStrategy);
        strategyMap.put(Strategy.X_WING, xWingStrategy);

        YWingStrategy yWingStrategy = new YWingStrategy();
        allStrategy.add(yWingStrategy);
        strategyMap.put(Strategy.Y_WING, yWingStrategy);

        SwordFishStrategy swordFishStrategy = new SwordFishStrategy();
        allStrategy.add(swordFishStrategy);
        strategyMap.put(Strategy.SWORDFISH, swordFishStrategy);
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

    public static String buildDesc(HintModel hintModel) {
        Strategy strategy = hintModel.getStrategy();
        FillStrategy fillStrategy = strategyMap.get(strategy);
        Assert.notNull(fillStrategy, "not null");
        return fillStrategy.buildDesc(hintModel);
    }

}
