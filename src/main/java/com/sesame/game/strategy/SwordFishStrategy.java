package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.Const;
import com.sesame.game.PuzzleTools;
import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.Direction;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction: 剑鱼
 *
 * @author sesame 2022/10/23
 */
public class SwordFishStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        //基于行的处理
        //找出每行的数字，满足这个数字在2个格子作为候选数
        List<Map<String, List<Position>>> allList = new ArrayList<>();
        for (int row = 0; row < Const.ROWS; row++) {
            List<Position> positionByRow = PuzzleTools.getPositionByRow(row);
            Map<String, List<Position>> collect = buildDigitalCountMap(remaining, positionByRow);
            if (!CollectionUtils.isEmpty(collect)) {
                allList.add(collect);
            }
        }

        if (allList.size() >= 3) {
            Optional<HintModel> result = buildHintModel(remaining, allList, Direction.ROW);
            if (result.isPresent()) {
                return result;
            }
        }

        //基于列的处理
        //找出每列的数字，满足这个数字在2个格子作为候选数
        allList = new ArrayList<>();
        for (int column = 0; column < Const.COLUMNS; column++) {
            List<Position> positionList = PuzzleTools.getPositionByColumn(column);
            Map<String, List<Position>> collect = buildDigitalCountMap(remaining, positionList);
            if (!CollectionUtils.isEmpty(collect)) {
                allList.add(collect);
            }
        }

        if (allList.size() >= 3) {
            return buildHintModel(remaining, allList, Direction.COLUMN);
        }

        return Optional.empty();
    }

    private Optional<HintModel> buildHintModel(Map<Position, List<String>> remaining,
        List<Map<String, List<Position>>> allList, Direction direction) {
        for (int i = 0; i < Const.VALID_VALUES.length; i++) {
            // 找出每个数字：满足在三行或者三列及以上，这个数字在2个格子作为候选数
            String digital = Const.VALID_VALUES[i];
            List<Map<String, List<Position>>> selectedList = allList.stream().filter(
                one -> one.containsKey(digital))
                .collect(Collectors.toList());
            int size = selectedList.size();
            if (size < 3) {
                continue;
            }

            for (int n = 0; n < size - 2; n++) {
                for (int m = n + 1; m < size - 1; m++) {
                    for (int p = m + 1; p < size; p++) {

                        List<Position> firstList = selectedList.get(n).get(digital);
                        List<Position> secondList = selectedList.get(m).get(digital);
                        List<Position> thirdList = selectedList.get(p).get(digital);
                        Assert.isTrue(firstList.size() == 2, "should be two");
                        Assert.isTrue(secondList.size() == 2, "should be two");
                        Assert.isTrue(thirdList.size() == 2, "should be two");
                        Map<Integer, Integer> countMap = checkSwordFish(firstList, secondList, thirdList, direction);
                        //检查这三列或三行中，是否存在数字为候选数
                        if (countMap.size() == 3) {
                            Map<Position, List<String>> deleteMap = new HashMap<>();
                            for (Integer index : countMap.keySet()) {

                                List<Position> positionList;
                                if (direction == Direction.ROW) {
                                    positionList = PuzzleTools.getPositionByColumn(index);
                                } else {
                                    positionList = PuzzleTools.getPositionByRow(index);
                                }

                                for (Position onePosition : positionList) {
                                    //排除本身
                                    if (firstList.contains(onePosition) || secondList.contains(
                                        onePosition) || thirdList.contains(onePosition)) {
                                        continue;
                                    }

                                    if (!CollectionUtils.isEmpty(remaining.get(onePosition)) && remaining.get(
                                        onePosition).contains(digital)) {
                                        List<String> deleteDigital = new ArrayList<>();
                                        deleteDigital.add(digital);
                                        deleteMap.put(onePosition, deleteDigital);
                                    }

                                }
                            }

                            //存在可删除的候选数，构建结果
                            if (!CollectionUtils.isEmpty(deleteMap)) {
                                Map<Position, List<String>> causeMap = new HashMap<>();

                                List<String> deleteDigital = new ArrayList<>();
                                deleteDigital.add(digital);
                                causeMap.put(firstList.get(0), deleteDigital);
                                deleteDigital = new ArrayList<>();
                                deleteDigital.add(digital);
                                causeMap.put(firstList.get(1), deleteDigital);

                                deleteDigital = new ArrayList<>();
                                deleteDigital.add(digital);
                                causeMap.put(secondList.get(0), deleteDigital);

                                deleteDigital = new ArrayList<>();
                                deleteDigital.add(digital);
                                causeMap.put(secondList.get(1), deleteDigital);

                                deleteDigital = new ArrayList<>();
                                deleteDigital.add(digital);
                                causeMap.put(thirdList.get(0), deleteDigital);

                                deleteDigital = new ArrayList<>();
                                deleteDigital.add(digital);
                                causeMap.put(thirdList.get(1), deleteDigital);

                                CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                                HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);

                                if (direction==Direction.ROW){
                                    List<UnitModel> unitModelList = new ArrayList<>();
                                    unitModelList.add(UnitModel.buildFromRow(firstList.get(0).getRow()));
                                    unitModelList.add(UnitModel.buildFromRow(secondList.get(0).getRow()));
                                    unitModelList.add(UnitModel.buildFromRow(thirdList.get(0).getRow()));
                                    result.of(unitModelList);
                                }else {
                                    List<UnitModel> unitModelList = new ArrayList<>();
                                    unitModelList.add(UnitModel.buildFromColumn(firstList.get(0).getCol()));
                                    unitModelList.add(UnitModel.buildFromColumn(secondList.get(0).getCol()));
                                    unitModelList.add(UnitModel.buildFromColumn(thirdList.get(0).getCol()));
                                    result.of(unitModelList);
                                }

                                return Optional.of(result);
                            }
                        }
                    }
                }
            }

        }
        return Optional.empty();
    }

    /**
     * 构建候选个数数量为2的Map，key为数字，value为对应的位置
     */
    private Map<String, List<Position>> buildDigitalCountMap(Map<Position, List<String>> remaining,
        List<Position> positionList) {
        //key 为数字，value为位置
        Map<String, List<Position>> countPerDigital = new HashMap<>();
        for (Position onePosition : positionList) {
            if (CollectionUtils.isEmpty(remaining.get(onePosition))) {
                continue;
            }
            remaining.get(onePosition).forEach(
                one -> {
                    if (countPerDigital.containsKey(one)) {
                        countPerDigital.get(one).add(onePosition);
                    } else {
                        List<Position> positions = new ArrayList<>();
                        positions.add(onePosition);
                        countPerDigital.put(one, positions);
                    }
                }
            );
        }
        return countPerDigital.entrySet().stream().filter(
            one -> one.getValue().size() == 2).collect(Collectors.toMap(
            one -> one.getKey(), one -> one.getValue()));
    }

    private Map<Integer, Integer> checkSwordFish(List<Position> firstList, List<Position> secondList,
        List<Position> thirdList, Direction direction) {

        Map<Integer, Integer> count = new HashMap<>(9);
        if (direction == Direction.ROW) {
            countColumn(count, firstList.get(0).getCol());
            countColumn(count, firstList.get(1).getCol());
            countColumn(count, secondList.get(0).getCol());
            countColumn(count, secondList.get(1).getCol());
            countColumn(count, thirdList.get(0).getCol());
            countColumn(count, thirdList.get(1).getCol());
        } else {
            countColumn(count, firstList.get(0).getRow());
            countColumn(count, firstList.get(1).getRow());
            countColumn(count, secondList.get(0).getRow());
            countColumn(count, secondList.get(1).getRow());
            countColumn(count, thirdList.get(0).getRow());
            countColumn(count, thirdList.get(1).getRow());
        }

        return count.entrySet().stream().filter(one -> one.getValue() == 2).collect(
            Collectors.toMap(one -> one.getKey(), one -> one.getValue()));
    }

    private void countColumn(Map<Integer, Integer> count, int index) {
        if (count.containsKey(index)) {
            count.put(index, count.get(index) + 1);
        } else {
            count.put(index, 1);
        }
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.SWORDFISH;
    }

    @Override
    public int priority() {
        return 0;
    }
}
