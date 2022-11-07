package com.sesame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sesame.game.strategy.model.Position;
import org.springframework.util.CollectionUtils;

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
     * key is the digital, the value is the position
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
}
