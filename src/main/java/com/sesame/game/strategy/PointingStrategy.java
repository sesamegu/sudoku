package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.CollectionUtils;

/**
 * Introduction: Pointing
 * When a Note is present twice or three in a block and this Note also belongs to the same row or column. This means
 * that the Note must be the solution for one of the two cells in the block. So, you can eliminate this Note from any
 * other cells in the row or column.
 *
 * @author sesame 2022/10/22
 */
public class PointingStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        //iterate the nine box
        for (int rowPoint = 0; rowPoint < Const.ROWS; rowPoint = rowPoint + Const.BOX_WIDTH) {
            for (int columnPoint = 0; columnPoint < Const.COLUMNS; columnPoint = columnPoint + Const.BOX_WIDTH) {

                List<Position> boxList = PuzzleTools.getPositionByBox(rowPoint, columnPoint);
                // check the three rows in the box
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
                //check the three columns in the box
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

            // at least two cells contain the candidate
            List<Position> causeList = threePosition.stream().filter(one ->
                !CollectionUtils.isEmpty(remaining.get(one)) && remaining.get(one).contains(oneDigital)
            ).collect(Collectors.toList());
            if (causeList.size() < 2) {
                continue;
            }

            //other cells doesn't contain the candidate
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

            // the row or column contains the candidate
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
    public String getDesc(HintModel hintModel) {
        return "";
    }

}
