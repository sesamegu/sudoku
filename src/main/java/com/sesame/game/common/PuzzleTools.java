package com.sesame.game.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Introduction: puzzle tools
 *
 * @author sesame 2022/10/17
 */
public abstract class PuzzleTools {

    public static List<Position> getPositionByRow(int row) {
        List<Position> positionList = new ArrayList<>(9);
        for (int column = 0; column < Const.COLUMNS; column++) {
            positionList.add(new Position(row, column));
        }
        return positionList;
    }

    public static List<Position> getPositionByColumn(int column) {
        List<Position> positionList = new ArrayList<>(9);
        for (int row = 0; row < Const.ROWS; row++) {
            positionList.add(new Position(row, column));
        }
        return positionList;
    }

    public static List<Position> getPositionByBox(int row, int column) {
        int rowStart = row - row % Const.BOX_WIDTH;
        int columnStart = column - column % Const.BOX_WIDTH;

        List<Position> positionList = new ArrayList<>(9);
        for (int i = rowStart; i < rowStart + Const.BOX_WIDTH; i++) {
            for (int j = columnStart; j < columnStart + Const.BOX_WIDTH; j++) {
                positionList.add(new Position(i, j));
            }
        }
        return positionList;
    }

    /**
     * key is the digital, the value is the count
     *
     * @param remaining
     * @param positionList
     * @return
     */
    public static Map<String, Integer> buildDigitalCountMap(Map<Position, List<String>> remaining,
        List<Position> positionList) {
        Map<String, Integer> countForDigital = new HashMap<>(9);
        for (Position onePosition : positionList) {
            List<String> digital = remaining.get(onePosition);
            if (CollectionUtils.isEmpty(digital)) {
                continue;
            }
            digital.forEach(oneDigital -> {
                if (countForDigital.containsKey(oneDigital)) {
                    countForDigital.put(oneDigital, countForDigital.get(oneDigital) + 1);
                } else {
                    countForDigital.put(oneDigital, 1);
                }
            });
        }
        return countForDigital;
    }

    /**
     * build key to position map
     *
     * @param remaining
     * @param positionList
     * @return key is the digital,value is its positions
     */
    public static Map<String, List<Position>> buildDigPosiMap(Map<Position, List<String>> remaining,
        List<Position> positionList) {
        Map<String, List<Position>> candidatePosition = new HashMap<>();
        for (Position onePosition : positionList) {
            List<String> candidates = remaining.get(onePosition);
            if (CollectionUtils.isEmpty(candidates)) {
                continue;
            }
            candidates.forEach(oneDigital -> {
                if (!candidatePosition.containsKey(oneDigital)) {
                    candidatePosition.put(oneDigital, new ArrayList<>());
                }
                candidatePosition.get(oneDigital).add(onePosition);
            });
        }
        return candidatePosition;
    }

    public static int calcBoxNumber(int row, int column) {
        int box;
        box = (row / Const.BOX_WIDTH) * 3;
        box += (column / Const.BOX_HEIGHT) + 1;
        return box;
    }


    public static int getNumber(UnitModel unitModel) {
        int number;
        if (Unit.ROW == unitModel.getUnit()) {
            number = unitModel.getRow() + 1;
        } else if (Unit.COLUMN == unitModel.getUnit()) {
            number = unitModel.getColumn() + 1;
        } else if (Unit.BOX == unitModel.getUnit()) {
            number = PuzzleTools.calcBoxNumber(unitModel.getRow(), unitModel.getColumn());
        } else {
            throw new RuntimeException("should be here");
        }
        return number;
    }
}
