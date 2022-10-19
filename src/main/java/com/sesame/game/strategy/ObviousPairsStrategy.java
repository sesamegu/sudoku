package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.Const;
import com.sesame.game.PuzzleTools;
import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:显性数对
 *
 * @author sesame 2022/10/16
 */
public class ObviousPairsStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        // 以行、列、宫为单元找出 显性数对
        Optional<HintModel> byRow = findByRow(remaining);
        if (byRow.isPresent()) {
            return byRow;
        }

        Optional<HintModel> byColumn = findByColumn(remaining);
        if (byColumn.isPresent()) {
            return byColumn;
        }

        Optional<HintModel> byBox = findByBox(remaining);
        if (byBox.isPresent()) {
            return byBox;
        }

        return Optional.empty();
    }

    private Optional<HintModel> findByRow(Map<Position, List<String>> remaining) {
        for (int row = 0; row < Const.ROWS; row++) {

            List<Position> positionList = PuzzleTools.getPositionByRow(row);
            Optional<HintModel> result = getHintModel(remaining, positionList);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    private Optional<HintModel> findByColumn(Map<Position, List<String>> remaining) {
        for (int column = 0; column < Const.COLUMNS; column++) {
            List<Position> positionList = PuzzleTools.getPositionByColumn(column);
            Optional<HintModel> result = getHintModel(remaining, positionList);
            if (result.isPresent()) {
                return result;
            }

        }
        return Optional.empty();
    }

    private Optional<HintModel> findByBox(Map<Position, List<String>> remaining) {
        for (int row = 0; row < Const.ROWS; row = row + Const.BOX_WIDTH) {
            for (int column = 0; column < Const.COLUMNS; column = column + Const.BOX_WIDTH) {
                List<Position> positionList = PuzzleTools.getPositionByBoxStart(row, column);
                Optional<HintModel> result = getHintModel(remaining, positionList);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    /**
     * 否存在显性数对
     *
     * @param remaining
     * @param positionList
     * @return
     */
    private Optional<HintModel> getHintModel(Map<Position, List<String>> remaining, List<Position> positionList) {
        //key 为数字字符串、value为之前的位置
        Map<String, Position> contains = new HashMap<>();
        for (Position position : positionList) {
            List<String> digital = remaining.get(position);
            if (CollectionUtils.isEmpty(digital) || digital.size() != 2) {
                continue;
            }

            String key = digital.get(0) + digital.get(1);

            if (contains.containsKey(key)) {
                //找到两个位置包含相同的候选值
                List<Position> twoSamePosition = new ArrayList<>();
                twoSamePosition.add(contains.get(key));
                twoSamePosition.add(position);

                //检查其它位置是否包含任意这两个任一值
                List<Position> related = new ArrayList<>();
                for (Position inner : positionList) {
                    if ((!twoSamePosition.contains(inner)) && (!CollectionUtils.isEmpty(
                        remaining.get(inner)))) {
                        List<String> innerDigital = remaining.get(inner);
                        if (innerDigital.contains(digital.get(0)) || innerDigital.contains(digital.get(1))) {
                            related.add(inner);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(related)) {
                    CandidateModel candidateModel = new CandidateModel(twoSamePosition, digital, related);
                    HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                    return Optional.of(result);
                }

            } else {
                contains.put(key, position);
            }

        }
        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.OBVIOUS_PAIRS;
    }

    @Override
    public int priority() {
        return 4;
    }
}
