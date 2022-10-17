package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.sesame.game.Const;
import com.sesame.game.SudokuPuzzle;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:隐性单一数 策略
 *
 * @author sesame 2022/10/14
 */
public class HiddenSinglesStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        // 以行、列、宫为单元找出隐形的单一数
        Optional<HintModel> byRow = findByRow(sudokuPuzzle, remaining);
        if (byRow.isPresent()) {
            return byRow;
        }

        Optional<HintModel> byColumn = findByColumn(sudokuPuzzle, remaining);
        if (byColumn.isPresent()) {
            return byColumn;
        }

        Optional<HintModel> byBox = findByBox(sudokuPuzzle, remaining);
        if (byBox.isPresent()) {
            return byBox;
        }

        return Optional.empty();
    }

    private Optional<HintModel> findByRow(SudokuPuzzle sudokuPuzzle, Map<Position, List<String>> remaining) {
        for (int row = 0; row < Const.ROWS; row++) {
            // 以行为单位，统计每个数字出现的次数。Map key 为数字，value为出现的次数
            List<Position> positionList = new ArrayList<>(9);
            for (int column = 0; column < Const.COLUMNS; column++) {
                positionList.add(new Position(row, column));
            }
            List<String> collect = statTimesByNumber(remaining, positionList);
            if (CollectionUtils.isEmpty(collect)) {
                continue;
            }

            for (String one : collect) {
                for (int column = 0; column < Const.COLUMNS; column++) {
                    Position position = new Position(row, column);
                    if (remaining.containsKey(position) && remaining.get(position).contains(one)) {
                        List<Position> related = findTheRelatedByRow(position, one, sudokuPuzzle);

                        //位置、值、相关点
                        HintModel result = HintModel.build().of(position)
                            .of(one).of(related).of(getStrategy());

                        return Optional.of(result);
                    }
                }
            }

        }

        return Optional.empty();
    }

    private Optional<HintModel> findByColumn(SudokuPuzzle sudokuPuzzle, Map<Position, List<String>> remaining) {
        for (int column = 0; column < Const.COLUMNS; column++) {

            // 以列单位，统计每个数字出现的次数。Map key 为数字，value为出现的次数
            List<Position> positionList = new ArrayList<>(9);
            for (int row = 0; row < Const.ROWS; row++) {
                positionList.add(new Position(row, column));
            }
            List<String> collect = statTimesByNumber(remaining, positionList);
            if (CollectionUtils.isEmpty(collect)) {
                continue;
            }

            for (String one : collect) {
                for (int row = 0; row < Const.ROWS; row++) {
                    Position position = new Position(row, column);
                    if (remaining.containsKey(position) && remaining.get(position).contains(one)) {
                        List<Position> related = findTheRelatedByColumn(position, one, sudokuPuzzle);

                        //位置、值、相关点
                        HintModel result = HintModel.build().of(position)
                            .of(one).of(related).of(getStrategy());

                        return Optional.of(result);
                    }
                }
            }

        }

        return Optional.empty();
    }

    private Optional<HintModel> findByBox(SudokuPuzzle sudokuPuzzle, Map<Position, List<String>> remaining) {
        for (int rowStartPoint = 0; rowStartPoint < Const.ROWS; rowStartPoint = rowStartPoint + Const.BOX_WIDTH) {
            for (int columnStartPoint = 0; columnStartPoint < Const.COLUMNS;
                columnStartPoint = columnStartPoint + Const.BOX_WIDTH) {
                Optional<HintModel> result = checkNineBox(sudokuPuzzle, rowStartPoint, columnStartPoint, remaining);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> checkNineBox(SudokuPuzzle sudokuPuzzle, int rowStartPoint, int columnStartPoint,
        Map<Position, List<String>> remaining) {

        // 以宫为单位，统计每个数字出现的次数。Map key 为数字，value为出现的次数
        List<Position> positionList = new ArrayList<>(9);
        for (int row = rowStartPoint; row < rowStartPoint + Const.BOX_WIDTH; row++) {
            for (int column = columnStartPoint; column < columnStartPoint + Const.BOX_WIDTH; column++) {
                positionList.add(new Position(row, column));
            }
        }

        List<String> collect = statTimesByNumber(remaining, positionList);
        if (CollectionUtils.isEmpty(collect)) {
            return Optional.empty();
        }

        for (String one : collect) {
            for (int row = rowStartPoint; row < rowStartPoint + Const.BOX_WIDTH; row++) {
                for (int column = columnStartPoint; column < columnStartPoint + Const.BOX_WIDTH; column++) {

                    Position position = new Position(row, column);
                    if (remaining.containsKey(position) && remaining.get(position).contains(one)) {
                        List<Position> related = findTheRelatedByBox(position, one, sudokuPuzzle);
                        //位置、值、相关点
                        HintModel result = HintModel.build().of(position)
                            .of(one).of(related).of(getStrategy());

                        return Optional.of(result);
                    }

                }
            }
        }

        throw new RuntimeException("should not be here.sudokuPuzzle " + sudokuPuzzle);
    }

    private List<String> statTimesByNumber(Map<Position, List<String>> remaining, List<Position> positionList) {
        // key 为数字，value为出现的次数
        Map<String, Integer> countForNumber = new HashMap<>(9);
        for (Position onePosition : positionList) {
            if (remaining.containsKey(onePosition)) {
                List<String> lefts = remaining.get(onePosition);
                lefts.stream().forEach(one -> {
                    if (countForNumber.containsKey(one)) {
                        countForNumber.put(one, countForNumber.get(one) + 1);
                    } else {
                        countForNumber.put(one, 1);
                    }
                });
            }
        }

        // 检查是否存在 某个数只有一个
        List<String> collect = countForNumber.entrySet().stream().filter(one -> one.getValue() == 1)
            .map(one -> one.getKey()).collect(Collectors.toList());

        //默认数字序
        Collections.sort(collect);
        return collect;

    }


    private List<Position> findTheRelatedByRow(Position position, String value, SudokuPuzzle sudokuPuzzle) {

        Set<Position> allRelated = new HashSet<>();
        // 找出本行所有空的单元格
        List<Position> allEmpty = new ArrayList<>();
        for (int column = 0; column < Const.COLUMNS; column++) {
            Position p = new Position(position.getRow(), column);
            if ((!sudokuPuzzle.isSlotValid(position.getRow(), column)) && column != position.getCol()) {
                allEmpty.add(p);
            }
            allRelated.add(p);
        }

        for (Position onePos : allEmpty) {

            //检查列是否包含数字
            Optional<Position> columnResult = sudokuPuzzle.numInColumnPosition(onePos.getCol(), value);
            if (columnResult.isPresent()) {
                allRelated.add(columnResult.get());
                continue;
            }

            //检查宫是否包含
            Optional<Position> boxResult = sudokuPuzzle.numInBoxPosition(onePos.getRow(), onePos.getCol(), value);
            if (boxResult.isPresent()) {
                allRelated.add(boxResult.get());
            }
        }

        allRelated.remove(position);
        return new ArrayList<>(allRelated);
    }

    private List<Position> findTheRelatedByColumn(Position position, String value, SudokuPuzzle sudokuPuzzle) {

        Set<Position> allRelated = new HashSet<>();
        // 找出本列所有空的单元格
        List<Position> allEmpty = new ArrayList<>();
        for (int row = 0; row < Const.COLUMNS; row++) {
            Position p = new Position(row, position.getCol());
            if ((!sudokuPuzzle.isSlotValid(row, position.getCol())) && row != position.getRow()) {
                allEmpty.add(p);
            }
            allRelated.add(p);
        }

        for (Position onePos : allEmpty) {

            //检查行是否包含数字
            Optional<Position> result = sudokuPuzzle.numInRowPosition(onePos.getRow(), value);
            if (result.isPresent()) {
                allRelated.add(result.get());
                continue;
            }

            //检查宫是否包含
            Optional<Position> boxResult = sudokuPuzzle.numInBoxPosition(onePos.getRow(), onePos.getCol(), value);
            if (boxResult.isPresent()) {
                allRelated.add(boxResult.get());
            }
        }

        allRelated.remove(position);
        return new ArrayList<>(allRelated);
    }

    private List<Position> findTheRelatedByBox(Position position, String value, SudokuPuzzle sudokuPuzzle) {

        Set<Position> allRelated = new HashSet<>();
        List<Position> allEmpty = new ArrayList<>();

        int row = position.getRow();
        int column = position.getCol();
        //找 出本宫所有空的单元格
        int rowStart = row - row % Const.BOX_WIDTH;
        int columnStart = column - column % Const.BOX_WIDTH;
        for (int i = rowStart; i < rowStart + Const.BOX_WIDTH; i++) {
            for (int j = columnStart; j < columnStart + Const.BOX_WIDTH; j++) {
                Position p = new Position(i, j);
                if ((!sudokuPuzzle.isSlotValid(i, j)) && (!p.equals(position))) {
                    allEmpty.add(p);
                }
                allRelated.add(p);
            }
        }

        for (Position onePos : allEmpty) {
            //检查行是否包含数字
            Optional<Position> result = sudokuPuzzle.numInRowPosition(onePos.getRow(), value);
            if (result.isPresent()) {
                allRelated.add(result.get());
                continue;
            }

            //检查列是否包含数字
            Optional<Position> columnResult = sudokuPuzzle.numInColumnPosition(onePos.getCol(), value);
            if (columnResult.isPresent()) {
                allRelated.add(columnResult.get());
            }
        }

        allRelated.remove(position);
        return new ArrayList<>(allRelated);
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_SINGLES;
    }

    @Override
    public int priority() {
        return 3;
    }
}
