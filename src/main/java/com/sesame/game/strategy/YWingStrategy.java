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
 * Introduction:Y翼，基于行列的有四种造型：右下、左下、右上、左上。基于宫的，有8个造型右下、左下、右上、左上、
 * 右右下、左左下、右右上、左左上
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

        Optional<HintModel> result = processRowHintModel(remaining, allRowList, allColumnList);
        if (result.isPresent()) {
            return result;
        }

        //以宫为单位，遍历9个宫
        for (int row = 0; row < Const.ROWS; row = row + Const.BOX_WIDTH) {
            for (int column = 0; column < Const.COLUMNS; column = column + Const.BOX_WIDTH) {

                //过滤出后续个数为2的位置列表
                List<Position> boxList = PuzzleTools.getPositionByBox(row, column);
                List<Position> filterList = boxList.stream().filter(one -> (!CollectionUtils.isEmpty(
                    remaining.get(one))) && remaining.get(one).size() == 2).collect(Collectors.toList());
                int size = filterList.size();
                if (size < 2) {
                    continue;
                }

                Optional<HintModel> hintModel = processBoxHintModel(remaining, filterList, row, column);
                if (hintModel.isPresent()) {
                    return hintModel;
                }

            }
        }
        return Optional.empty();

    }

    private Optional<HintModel> processBoxHintModel(Map<Position, List<String>> remaining,
        List<Position> filterList, int row, int column) {
        int size = filterList.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                Position first = filterList.get(i);
                Position second = filterList.get(j);
                //不在同行同列
                if (first.getCol() == second.getCol() || first.getRow() == second.getRow()) {
                    continue;
                }

                //两个候选数一个是相同，一个不同
                List<String> sameDigital = new ArrayList<>(remaining.get(first));
                sameDigital.retainAll(remaining.get(second));
                if (sameDigital.size() != 1) {
                    continue;
                }

                //找出第三格需要匹配的候选数组
                List<String> thirdDigital = new ArrayList<>();
                thirdDigital.addAll(remaining.get(first));
                thirdDigital.addAll(remaining.get(second));
                thirdDigital.removeAll(sameDigital);
                Assert.isTrue(thirdDigital.size() == 2, "should be two");
                Collections.sort(thirdDigital);

                // 查找第一个数列：因为第1列和第2列位置不定，因此同样存在四种造型情况
                List<String> tempDigital = new ArrayList<>(thirdDigital);
                tempDigital.removeAll(remaining.get(first));
                Assert.isTrue(tempDigital.size() == 1, "should be one");
                String firstDeleteDigital = tempDigital.get(0);

                List<Position> positionByColumn = PuzzleTools.getPositionByColumn(first.getCol());
                Optional<HintModel> hintModel = boxBuildHintModel(remaining, row, column, first, second,
                    thirdDigital, positionByColumn, firstDeleteDigital, Direction.COLUMN, second.getCol(), -1);
                if (hintModel.isPresent()) {
                    return hintModel;
                }

                // 查找第二数列：因为第1列和第2列位置不定，因此同样存在四种造型情况
                tempDigital = new ArrayList<>(thirdDigital);
                tempDigital.removeAll(remaining.get(second));
                Assert.isTrue(tempDigital.size() == 1, "should be one");
                String secondDeleteDigital = tempDigital.get(0);
                positionByColumn = PuzzleTools.getPositionByColumn(second.getCol());
                hintModel = boxBuildHintModel(remaining, row, column, first, second,
                    thirdDigital, positionByColumn, secondDeleteDigital, Direction.COLUMN, first.getCol(), -1);
                if (hintModel.isPresent()) {
                    return hintModel;
                }

                //  查找第一个行数：因为第1行和第2行位置不定，因此同样存在四种造型情况
                List<Position> positionByRow = PuzzleTools.getPositionByRow(first.getRow());
                hintModel = boxBuildHintModel(remaining, row, column, first, second,
                    thirdDigital, positionByRow, firstDeleteDigital, Direction.ROW, -1, second.getRow());
                if (hintModel.isPresent()) {
                    return hintModel;
                }

                //  查找第二个行数：因为第1行和第2行位置不定，因此同样存在四种造型情况
                positionByRow = PuzzleTools.getPositionByRow(second.getRow());
                hintModel = boxBuildHintModel(remaining, row, column, first, second,
                    thirdDigital, positionByRow, firstDeleteDigital, Direction.ROW, -1, first.getRow());
                if (hintModel.isPresent()) {
                    return hintModel;
                }
            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> boxBuildHintModel(Map<Position, List<String>> remaining, int row, int column,
        Position first, Position second, List<String> thirdDigital, List<Position> positionList,
        String deleteDigital, Direction direction, int forthColumn, int forthRow) {
        for (Position third : positionList) {
            //过滤同宫的位置
            if (direction == Direction.COLUMN) {
                int thirdRow = third.getRow();
                if (thirdRow == row || thirdRow == row + 1 || thirdRow == row + 2) {
                    continue;
                }
            } else {
                int thirdColumn = third.getCol();
                if (thirdColumn == column || thirdColumn == column + 1 || thirdColumn == column + 2) {
                    continue;
                }
            }

            if (CollectionUtils.isEmpty(remaining.get(third))) {
                continue;
            }

            List<String> thirdRemain = new ArrayList<>(remaining.get(third));
            if (!thirdRemain.equals(thirdDigital)) {
                continue;
            }

            //尝试Y翼的位置
            List<Position> possiblePosition = new ArrayList<>();
            if (direction == Direction.COLUMN) {
                // 第四个位置有多重可能性：除了Y翼的位置，还有两个位置：在这个宫且列和第三个数的列 相同的两个位置
                possiblePosition.add(new Position(third.getRow(), forthColumn));

                List<Position> threeColumnInBox = new ArrayList<>(3);
                threeColumnInBox.add(new Position(row, third.getCol()));
                threeColumnInBox.add(new Position(row + 1, third.getCol()));
                threeColumnInBox.add(new Position(row + 2, third.getCol()));

                //排除原来的数
                threeColumnInBox.remove(first);
                threeColumnInBox.remove(second);
                Assert.isTrue(threeColumnInBox.size() == 2, "should be two");
                possiblePosition.addAll(threeColumnInBox);
            } else {
                // 第四个位置有多重可能性：除了Y翼的位置，还有两个位置：在这个宫且列和第三个数的行 相同的两个位置
                possiblePosition.add(new Position(forthRow, third.getCol()));

                List<Position> threeColumnInBox = new ArrayList<>(3);
                threeColumnInBox.add(new Position(third.getRow(), column));
                threeColumnInBox.add(new Position(third.getRow(), column + 1));
                threeColumnInBox.add(new Position(third.getRow(), column + 2));

                //排除原来的数
                threeColumnInBox.remove(first);
                threeColumnInBox.remove(second);
                Assert.isTrue(threeColumnInBox.size() == 2, "should be two");
                possiblePosition.addAll(threeColumnInBox);
            }

            for (Position forth : possiblePosition) {
                if (!CollectionUtils.isEmpty(remaining.get(forth)) && remaining.get(forth).contains(
                    deleteDigital)) {
                    Map<Position, List<String>> causeMap = new HashMap<>();
                    causeMap.put(first, new ArrayList<>(remaining.get(first)));
                    causeMap.put(second, new ArrayList<>(remaining.get(second)));
                    causeMap.put(third, new ArrayList<>(remaining.get(third)));

                    Map<Position, List<String>> deleteMap = new HashMap<>();
                    List<String> forthDigital = new ArrayList<>();
                    forthDigital.add(deleteDigital);
                    deleteMap.put(forth, forthDigital);
                    CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                    HintModel tt = HintModel.build().of(getStrategy()).of(candidateModel);
                    return Optional.of(tt);
                }
            }

        }
        return Optional.empty();
    }

    private Optional<HintModel> processRowHintModel(Map<Position, List<String>> remaining,
        List<List<Position>> allList, List<List<Position>> allColumnList) {
        if (allList.size() < 2 || allColumnList.size() < 2) {
            return Optional.empty();
        }

        // 对每行进行双循环遍历
        for (int i = 0; i < allList.size(); i++) {
            List<Position> positionList = allList.get(i);
            if (positionList.size() < 2) {
                continue;
            }
            for (int n = 0; n < positionList.size() - 1; n++) {
                for (int m = n + 1; m < positionList.size(); m++) {

                    Position first = positionList.get(n);
                    Position second = positionList.get(m);
                    //找出有共同一个数字的两个位置
                    List<String> causeDigital = new ArrayList<>(remaining.get(first));
                    causeDigital.retainAll(remaining.get(second));
                    if (causeDigital.size() != 1) {
                        continue;
                    }
                    //找出第三格需要匹配的候选数组
                    List<String> thirdDigital = new ArrayList<>();
                    thirdDigital.addAll(remaining.get(first));
                    thirdDigital.addAll(remaining.get(second));
                    thirdDigital.removeAll(causeDigital);
                    Assert.isTrue(thirdDigital.size() == 2, "should be two");
                    Collections.sort(thirdDigital);

                    // 右下、右上：查找前列，是否存在一个单元格剩余数符合
                    List<String> tempDigital = new ArrayList<>(thirdDigital);
                    tempDigital.removeAll(remaining.get(first));
                    Assert.isTrue(tempDigital.size() == 1, "should be one");
                    String deleteDigital = tempDigital.get(0);

                    Optional<HintModel> result = buildHintModel(remaining, first, second,
                        thirdDigital, deleteDigital, first.getCol(), second.getCol());
                    if (result.isPresent()) {
                        return result;
                    }

                    tempDigital = new ArrayList<>(thirdDigital);
                    tempDigital.removeAll(remaining.get(second));
                    Assert.isTrue(tempDigital.size() == 1, "should be one");
                    deleteDigital = tempDigital.get(0);

                    // 左下、左上：查找后列，是否存在一个单元格剩余数符合
                    result = buildHintModel(remaining, first, second,
                        thirdDigital, deleteDigital, second.getCol(), first.getCol());
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
