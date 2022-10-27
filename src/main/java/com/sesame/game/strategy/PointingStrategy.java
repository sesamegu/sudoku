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
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.CollectionUtils;

/**
 * Introduction: 宫策略（宫对、宫三）
 *
 * @author sesame 2022/10/22
 */
public class PointingStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> tryStrategy(SudokuPuzzle sudokuPuzzle) {
        //对9个宫依次遍历
        for (int rowPoint = 0; rowPoint < Const.ROWS; rowPoint = rowPoint + Const.BOX_WIDTH) {
            for (int columnPoint = 0; columnPoint < Const.COLUMNS; columnPoint = columnPoint + Const.BOX_WIDTH) {

                List<Position> boxList = PuzzleTools.getPositionByBox(rowPoint, columnPoint);
                //对每个宫内的3行处理
                for (int row = rowPoint; row < rowPoint + 3; row++) {
                    List<Position> threePosition = new ArrayList<>(3);
                    threePosition.add(new Position(row, columnPoint));
                    threePosition.add(new Position(row, columnPoint + 1));
                    threePosition.add(new Position(row, columnPoint + 2));

                    List<Position> rowList = PuzzleTools.getPositionByRow(row);
                    Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
                    Optional<HintModel> result = checkBox(remaining, boxList, rowList, threePosition);
                    if (result.isPresent()) {
                        List<UnitModel> unitModelList = new ArrayList<>();
                        unitModelList.add(UnitModel.buildFromBox(rowPoint, columnPoint));
                        unitModelList.add(UnitModel.buildFromRow(row));
                        result.get().of(unitModelList);

                        return result;
                    }
                }
                //对每个宫内的3列依次处理
                for (int column = columnPoint; column < columnPoint + 3; column++) {
                    List<Position> threePosition = new ArrayList<>(3);
                    threePosition.add(new Position(rowPoint, column));
                    threePosition.add(new Position(rowPoint + 1, column));
                    threePosition.add(new Position(rowPoint + 2, column));

                    List<Position> columnList = PuzzleTools.getPositionByColumn(column);
                    Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
                    Optional<HintModel> result = checkBox(remaining, boxList, columnList, threePosition);
                    if (result.isPresent()) {
                        List<UnitModel> unitModelList = new ArrayList<>();
                        unitModelList.add(UnitModel.buildFromBox(rowPoint, columnPoint));
                        unitModelList.add(UnitModel.buildFromColumn(column));
                        result.get().of(unitModelList);
                        return result;
                    }
                }

            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> checkBox(Map<Position, List<String>> remaining, List<Position> boxList,
        List<Position> relatedList, List<Position> threePosition) {

        for (int i = 0; i < Const.VALID_VALUES.length; i++) {
            String oneDigital = Const.VALID_VALUES[i];

            //在三个单元格中至少2个单元格包含
            List<Position> causeList = threePosition.stream().filter(one ->
                !CollectionUtils.isEmpty(remaining.get(one)) && remaining.get(one).contains(oneDigital)
            ).collect(Collectors.toList());
            if (causeList.size() < 2) {
                continue;
            }

            //宫的其它单元格不包含这个数字
            boolean isContainInOther = false;
            for (Position onePos : boxList) {
                if (threePosition.contains(onePos)) {
                    continue;
                }
                if (!CollectionUtils.isEmpty(remaining.get(onePos)) && remaining.get(onePos).contains(oneDigital)) {
                    isContainInOther = true;
                }
            }
            if (isContainInOther) {
                continue;
            }

            // 行或者列的列表包含这个数字
            List<Position> deletePosition = new ArrayList<>();
            for (Position onePos : relatedList) {
                if (threePosition.contains(onePos)) {
                    continue;
                }
                if (!CollectionUtils.isEmpty(remaining.get(onePos)) && remaining.get(onePos).contains(oneDigital)) {
                    deletePosition.add(onePos);
                }
            }

            if (!CollectionUtils.isEmpty(deletePosition)) {
                Map<Position, List<String>> causeMap = new HashMap<>();
                causeList.forEach(
                    one -> {
                        List<String> digitalList = new ArrayList<>();
                        digitalList.add(oneDigital);
                        causeMap.put(one, digitalList);
                    });

                Map<Position, List<String>> deleteMap = new HashMap<>();
                deletePosition.forEach(
                    one -> {
                        List<String> digitalList = new ArrayList<>();
                        digitalList.add(oneDigital);
                        deleteMap.put(one, digitalList);
                    }
                );

                CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                return Optional.of(result);
            }

        }

        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.POINTING_PAIRS;
    }

    @Override
    public int priority() {
        return 0;
    }
}
