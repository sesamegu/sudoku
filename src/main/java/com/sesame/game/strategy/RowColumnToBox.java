package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.Direction;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:RowColumnToBox A candidate in a row or column, and all the cell also in a box. Then we can delete the
 * candidate in other cells in the box.
 *
 * @author sesame 2022/11/18
 */
public class RowColumnToBox implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        // iterate the rows
        for (int row = 0; row < Const.ROWS; row++) {
            List<Position> positionByRow = PuzzleTools.getPositionByRow(row);
            Map<String, List<Position>> candidatePosition = PuzzleTools.buildDigPosiMap(remaining, positionByRow);
            if (CollectionUtils.isEmpty(candidatePosition)) {
                continue;
            }

            //find the box if exists
            for (Entry<String, List<Position>> oneEntry : candidatePosition.entrySet()) {
                // check all positions in a box
                List<Position> causeList = oneEntry.getValue();
                boolean allSame = checkInSameBox(causeList);
                if (allSame) {
                    // check other cells contain the candidate
                    String key = oneEntry.getKey();
                    List<Position> deletePosition = checkOtherCells(remaining, causeList, key);
                    //build the result
                    if (!CollectionUtils.isEmpty(deletePosition)) {
                        //cause map
                        return buildHintModel(row, -1, causeList, key, deletePosition, Direction.ROW);
                    }
                }
            }
        }

        // iterate the columns
        for (int column = 0; column < Const.COLUMNS; column++) {
            List<Position> positionByColumn = PuzzleTools.getPositionByColumn(column);
            Map<String, List<Position>> candidatePosition = PuzzleTools.buildDigPosiMap(remaining, positionByColumn);
            if (CollectionUtils.isEmpty(candidatePosition)) {
                continue;
            }

            //find the box if exists
            for (Entry<String, List<Position>> oneEntry : candidatePosition.entrySet()) {
                // check all positions in a box
                List<Position> causeList = oneEntry.getValue();
                boolean allSame = checkInSameBox(causeList);
                if (allSame) {
                    // check other cells contain the candidate
                    String key = oneEntry.getKey();
                    List<Position> deletePosition = checkOtherCells(remaining, causeList, key);
                    //build the result
                    if (!CollectionUtils.isEmpty(deletePosition)) {
                        //cause map
                        return buildHintModel(-1, column, causeList, key, deletePosition, Direction.COLUMN);
                    }
                }
            }
        }

        return Optional.empty();
    }

    private List<Position> checkOtherCells(Map<Position, List<String>> remaining, List<Position> causeList,
        String key) {
        Position first = causeList.get(0);
        List<Position> positionByBox = PuzzleTools.getPositionByBox(first.getRow(), first.getCol());
        List<Position> deletePosition = new ArrayList<>();
        for (Position boxPosition : positionByBox) {
            if (causeList.contains(boxPosition)) {
                continue;
            }

            List<String> candidates = remaining.get(boxPosition);
            if (!CollectionUtils.isEmpty(candidates) && candidates.contains(key)) {
                deletePosition.add(boxPosition);
            }
        }
        return deletePosition;
    }

    private boolean checkInSameBox(List<Position> causeList) {
        int colIndex = causeList.get(0).getCol() / Const.BOX_WIDTH;
        for (Position position : causeList) {
            if (colIndex != position.getCol() / Const.BOX_WIDTH) {
                return false;
            }
        }

        int rowIndex = causeList.get(0).getRow() / Const.BOX_HEIGHT;
        for (Position position : causeList) {
            if (rowIndex != position.getRow() / Const.BOX_HEIGHT) {
                return false;
            }
        }

        return true;
    }

    private Optional<HintModel> buildHintModel(int row, int column, List<Position> causeList, String key,
        List<Position> deletePosition, Direction direction) {
        Map<Position, List<String>> causeMap = new HashMap<>();
        causeList.forEach(
            one -> {
                List<String> digitalList = new ArrayList<>();
                digitalList.add(key);
                causeMap.put(one, digitalList);
            });

        //delete map
        Map<Position, List<String>> deleteMap = new HashMap<>();
        deletePosition.forEach(
            one -> {
                List<String> digitalList = new ArrayList<>();
                digitalList.add(key);
                deleteMap.put(one, digitalList);
            }
        );

        //build the result
        CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
        HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
        List<UnitModel> unitModelList = new ArrayList<>();
        if (Direction.ROW == direction) {
            unitModelList.add(UnitModel.buildFromRow(row));
        } else {
            unitModelList.add(UnitModel.buildFromColumn(column));
        }
        Position first = causeList.get(0);
        int rowStart = first.getRow() - first.getRow() % Const.BOX_WIDTH;
        int columnStart = first.getCol() - first.getCol() % Const.BOX_WIDTH;
        unitModelList.add(UnitModel.buildFromBox(rowStart, columnStart));
        result.of(unitModelList);

        return Optional.of(result);
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.ROW_COLUMN_TO_BOX;
    }

    @Override
    public String buildDesc(HintModel hintModel) {
        Assert.isTrue(hintModel.getUnitModelList().size() == 2, "should be 2");
        UnitModel rowOrColumnModel = hintModel.getUnitModelList().get(0);
        UnitModel boxModel = hintModel.getUnitModelList().get(1);
        int rowOrColumnNumber = PuzzleTools.getNumber(rowOrColumnModel);
        int boxNumber = PuzzleTools.getNumber(boxModel);
        Map<Position, List<String>> causeMap = hintModel.getCandidateModel().getCauseMap();
        List<String> digitalList = causeMap.values().iterator().next();
        Assert.isTrue(digitalList.size() == 1, "should be 1 ");
        String digital = digitalList.get(0);

        return I18nProcessor.getAppendValue(getStrategy().getName() + "_hint",
            rowOrColumnNumber,
            I18nProcessor.getValue(rowOrColumnModel.getUnit().getDesc()),
            digital,
            boxNumber
        );
    }

}
