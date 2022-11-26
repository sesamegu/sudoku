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

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.Direction;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.Validate;

/**
 * Introduction: Sword Fish
 * "Swordfish" is similar to X-wing but uses three sets of cells instead of two.
 *
 * @author sesame 2022/10/23
 */
public class SwordFishStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        //by row
        //find the digital which is two cell's candidate
        List<Map<String, List<Position>>> allList = new ArrayList<>();
        for (int row = 0; row < Const.ROWS; row++) {
            List<Position> positionByRow = PuzzleTools.getPositionByRow(row);
            Map<String, List<Position>> collect = buildDigitalCountMap(remaining, positionByRow);
            if (!MapUtils.isEmpty(collect)) {
                allList.add(collect);
            }
        }

        if (allList.size() >= 3) {
            Optional<HintModel> result = buildHintModel(remaining, allList, Direction.ROW);
            if (result.isPresent()) {
                return result;
            }
        }

        //by column
        //find the digital which is two cell's candidate
        allList = new ArrayList<>();
        for (int column = 0; column < Const.COLUMNS; column++) {
            List<Position> positionList = PuzzleTools.getPositionByColumn(column);
            Map<String, List<Position>> collect = buildDigitalCountMap(remaining, positionList);
            if (!MapUtils.isEmpty(collect)) {
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
            // find the digital appear in three rows or three columns
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
                        Validate.isTrue(firstList.size() == 2, "should be two");
                        Validate.isTrue(secondList.size() == 2, "should be two");
                        Validate.isTrue(thirdList.size() == 2, "should be two");
                        Map<Integer, Integer> countMap = checkSwordFish(firstList, secondList, thirdList, direction);
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

                            // check if the candidate exists in the other cells
                            if (!MapUtils.isEmpty(deleteMap)) {
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

                                List<UnitModel> unitModelList = new ArrayList<>();
                                if (direction == Direction.ROW) {
                                    unitModelList.add(UnitModel.buildFromRow(firstList.get(0).getRow()));
                                    unitModelList.add(UnitModel.buildFromRow(secondList.get(0).getRow()));
                                    unitModelList.add(UnitModel.buildFromRow(thirdList.get(0).getRow()));
                                } else {
                                    unitModelList.add(UnitModel.buildFromColumn(firstList.get(0).getCol()));
                                    unitModelList.add(UnitModel.buildFromColumn(secondList.get(0).getCol()));
                                    unitModelList.add(UnitModel.buildFromColumn(thirdList.get(0).getCol()));
                                }
                                result.of(unitModelList);

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
     * key is the digital, value is the position
     */
    private Map<String, List<Position>> buildDigitalCountMap(Map<Position, List<String>> remaining,
        List<Position> positionList) {
        Map<String, List<Position>> countPerDigital = PuzzleTools.buildDigPosiMap(remaining, positionList);
        return countPerDigital.entrySet().stream().filter(one -> one.getValue().size() == 2).collect(Collectors.toMap(
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
    public String buildDesc(HintModel hintModel) {
        //在第{0}、{1}、{2}{3}中数字{4}都只出现了两次，且这三{3}的位置两两对齐，那么可以删除第{5}、{6}、{7}{8}的数字{4}
        CandidateModel candidateModel = hintModel.getCandidateModel();
        Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
        String deleteDigital = deleteMap.values().iterator().next().get(0);

        Validate.isTrue(hintModel.getUnitModelList().size() == 3, "should be 3");
        UnitModel unitModelOne = hintModel.getUnitModelList().get(0);
        UnitModel unitModelTwo = hintModel.getUnitModelList().get(1);
        UnitModel unitModelThree = hintModel.getUnitModelList().get(2);

        int firstNumber = PuzzleTools.getNumber(unitModelOne);
        int secondNumber = PuzzleTools.getNumber(unitModelTwo);
        int thirdNumber = PuzzleTools.getNumber(unitModelThree);

        Map<Position, List<String>> causeMap = candidateModel.getCauseMap();
        Validate.isTrue(causeMap.size() == 6, "should be 6");
        List<Integer> rowOrColumns;

        Unit currentUnit = unitModelOne.getUnit();
        Unit oppositeUnit;
        if (Unit.ROW == currentUnit) {
            oppositeUnit = Unit.COLUMN;
            Set<Integer> columns = new HashSet<>(
                causeMap.keySet().stream().map(Position::getCol).collect(Collectors.toList()));
            rowOrColumns = new ArrayList<>(columns);
            Collections.sort(rowOrColumns);
            Validate.isTrue(rowOrColumns.size() == 3, "should be 3");
        } else if (Unit.COLUMN == currentUnit) {
            oppositeUnit = Unit.ROW;
            Set<Integer> rows = new HashSet<>(
                causeMap.keySet().stream().map(Position::getRow).collect(Collectors.toList()));
            rowOrColumns = new ArrayList<>(rows);
            Collections.sort(rowOrColumns);
            Validate.isTrue(rowOrColumns.size() == 3, "should be 3");
        } else {
            throw new RuntimeException("should not be here.");
        }

        return I18nProcessor.getAppendValue(getStrategy().getName() + "_hint",
            firstNumber,
            secondNumber,
            thirdNumber,
            I18nProcessor.getValue(currentUnit.getDesc()),
            deleteDigital,
            rowOrColumns.get(0) + 1,
            rowOrColumns.get(1) + 1,
            rowOrColumns.get(2) + 1,
            I18nProcessor.getValue(oppositeUnit.getDesc())
        );

    }

}
