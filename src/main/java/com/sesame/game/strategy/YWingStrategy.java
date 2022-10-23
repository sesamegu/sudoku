package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.Const;
import com.sesame.game.PuzzleTools;
import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:Y翼，有四种造型：右下、左下、右上、左上
 *
 * @author sesame 2022/10/23
 */
public class YWingStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        // 基于每行，找出候选个数数量为2的所有位置
        List<List<Position>> allRowList = new ArrayList<>();
        for (int i = 0; i < Const.ROWS; i++) {
            List<Position> rowList = PuzzleTools.getPositionByRow(i);
            List<Position> collect = rowList.stream().filter(onePosition ->
                !CollectionUtils.isEmpty(remaining.get(onePosition)) && remaining.get(onePosition).size()
                    == 2).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                allRowList.add(collect);
            }
        }

        // 基于每列，找出候选个数数量为2的所有位置
        List<List<Position>> allColumnList = new ArrayList<>();
        for (int i = 0; i < Const.COLUMNS; i++) {
            List<Position> columnList = PuzzleTools.getPositionByColumn(i);
            List<Position> collect = columnList.stream().filter(onePosition ->
                !CollectionUtils.isEmpty(remaining.get(onePosition)) && remaining.get(onePosition).size()
                    == 2).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                allColumnList.add(collect);
            }
        }

        if (allRowList.size() < 2 || allColumnList.size() < 2) {
            return Optional.empty();
        }

        return processRowHintModel(remaining, allRowList);

    }

    private Optional<HintModel> processRowHintModel(Map<Position, List<String>> remaining,
        List<List<Position>> allList) {
        // 对每行进行双循环遍历
        for (int i = 0; i < allList.size(); i++) {
            List<Position> positionList = allList.get(i);
            if (positionList.size() < 2) {
                continue;
            }
            for (int n = 0; n < positionList.size() - 1; n++) {
                for (int m = n + 1; m < positionList.size(); m++) {

                    Position front = positionList.get(n);
                    Position behind = positionList.get(m);
                    //找出有共同一个数字的两个位置
                    List<String> causeDigital = new ArrayList<>(remaining.get(front));
                    causeDigital.retainAll(remaining.get(behind));
                    if (causeDigital.size() != 1) {
                        continue;
                    }
                    //找出第三格需要匹配的候选数组
                    List<String> thirdDigital = new ArrayList<>();
                    thirdDigital.addAll(remaining.get(front));
                    thirdDigital.addAll(remaining.get(behind));
                    thirdDigital.removeAll(causeDigital);
                    Assert.isTrue(thirdDigital.size() == 2, "should be two");
                    Collections.sort(thirdDigital);

                    // 右下、右上：查找前列，是否存在一个单元格剩余数符合
                    List<String> tempDigital = new ArrayList<>(thirdDigital);
                    tempDigital.removeAll(remaining.get(front));
                    Assert.isTrue(tempDigital.size() == 1, "should be one");
                    String deleteDigital = tempDigital.get(0);
                    Optional<HintModel> result = buildHintModel(remaining, front, behind,
                        thirdDigital, deleteDigital, front.getCol(), behind.getCol());
                    if (result.isPresent()) {
                        return result;
                    }

                    tempDigital = new ArrayList<>(thirdDigital);
                    tempDigital.removeAll(remaining.get(behind));
                    Assert.isTrue(tempDigital.size() == 1, "should be one");
                    deleteDigital = tempDigital.get(0);

                    // 左下、左上：查找后列，是否存在一个单元格剩余数符合
                    result = buildHintModel(remaining, front, behind,
                        thirdDigital, deleteDigital, behind.getCol(), front.getCol());
                    if (result.isPresent()) {
                        return result;
                    }

                }
            }
        }
        return Optional.empty();
    }

    private Optional<HintModel> buildHintModel(Map<Position, List<String>> remaining, Position front, Position behind,
        List<String> thirdDigital, String deleteDigital, int thirdColumn, int forthColumn) {
        List<Position> positionByColumn = PuzzleTools.getPositionByColumn(thirdColumn);
        for (Position third : positionByColumn) {
            if (CollectionUtils.isEmpty(remaining.get(third))) {
                continue;
            }
            // 检查第三格单元格候选、第四格是否包含 删除数
            if (remaining.get(third).equals(thirdDigital)) {
                Position forth = new Position(third.getRow(), forthColumn);
                if (!CollectionUtils.isEmpty(remaining.get(forth)) && remaining.get(forth).contains(
                    deleteDigital)) {
                    Map<Position, List<String>> causeMap = new HashMap<>();
                    causeMap.put(front, new ArrayList<>(remaining.get(front)));
                    causeMap.put(behind, new ArrayList<>(remaining.get(behind)));
                    causeMap.put(third, new ArrayList<>(remaining.get(third)));

                    Map<Position, List<String>> deleteMap = new HashMap<>();
                    List<String> forthDigital = new ArrayList<>();
                    forthDigital.add(deleteDigital);
                    deleteMap.put(forth, forthDigital);
                    CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                    HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                    return Optional.of(result);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.Y_WING;
    }

    @Override
    public int priority() {
        return 0;
    }
}
