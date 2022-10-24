package com.sesame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sesame.game.strategy.Position;
import org.springframework.util.CollectionUtils;

/**
 * Introduction: 工具类
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

    /**
     * @param rowStartPoint 行的开始位置，为0，3或者6
     * @param columnStartPoint 列的开始位置，为0，3或者6
     * @return
     */
    public static List<Position> getPositionByBoxStart(int rowStartPoint, int columnStartPoint) {
        //TODO 与下面的方法getPositionByBoxAny合并
        List<Position> positionList  = new ArrayList<>(9);
        for (int row = rowStartPoint; row < rowStartPoint + Const.BOX_WIDTH; row++) {
            for (int column = columnStartPoint; column < columnStartPoint + Const.BOX_WIDTH; column++) {
                positionList.add(new Position(row, column));
            }
        }
        return positionList;
    }

    /**
     * @param row 行的位置，任意位置
     * @param column 列的位置，任意位置
     * @return
     */
    public static List<Position> getPositionByBoxAny(int row, int column) {
        int rowStart = row - row % Const.BOX_WIDTH;
        int columnStart = column - column % Const.BOX_WIDTH;
        return getPositionByBoxStart(rowStart, columnStart);
    }

    /**
     * 返回字母以及对应出现的次数
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
