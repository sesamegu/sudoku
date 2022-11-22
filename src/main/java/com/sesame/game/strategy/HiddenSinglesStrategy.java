package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
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
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:Hidden Singles
 * The point is that in a specific cell only one digit (from the Notes) remains possible
 *
 * @author sesame 2022/10/14
 */
public class HiddenSinglesStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();

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
            List<Position> positionList = PuzzleTools.getPositionByRow(row);
            List<String> collect = findAppearOnce(remaining, positionList);
            if (CollectionUtils.isEmpty(collect)) {
                continue;
            }

            for (String one : collect) {
                for (Position position : positionList) {
                    if (remaining.containsKey(position) && remaining.get(position).contains(one)) {
                        List<Position> related = findTheRelatedByRow(position, one, sudokuPuzzle);

                        // build the result
                        SolutionModel solutionModel = new SolutionModel(position, one, related);
                        HintModel result = HintModel.build().of(solutionModel).of(getStrategy());

                        List<UnitModel> unitModelList = new ArrayList<>();
                        unitModelList.add(UnitModel.buildFromRow(row));
                        result.of(unitModelList);

                        return Optional.of(result);
                    }
                }
            }

        }

        return Optional.empty();
    }

    private Optional<HintModel> findByColumn(SudokuPuzzle sudokuPuzzle, Map<Position, List<String>> remaining) {
        for (int column = 0; column < Const.COLUMNS; column++) {
            List<Position> positionList = PuzzleTools.getPositionByColumn(column);
            List<String> collect = findAppearOnce(remaining, positionList);
            if (CollectionUtils.isEmpty(collect)) {
                continue;
            }

            for (String one : collect) {
                for (Position position : positionList) {
                    if (remaining.containsKey(position) && remaining.get(position).contains(one)) {
                        List<Position> related = findTheRelatedByColumn(position, one, sudokuPuzzle);

                        // build the result
                        SolutionModel solutionModel = new SolutionModel(position, one, related);
                        HintModel result = HintModel.build().of(solutionModel).of(getStrategy());

                        List<UnitModel> unitModelList = new ArrayList<>();
                        unitModelList.add(UnitModel.buildFromColumn(column));
                        result.of(unitModelList);

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

        List<Position> positionList = PuzzleTools.getPositionByBox(rowStartPoint, columnStartPoint);
        List<String> collect = findAppearOnce(remaining, positionList);
        if (CollectionUtils.isEmpty(collect)) {
            return Optional.empty();
        }

        for (String one : collect) {
            for (Position position : positionList) {
                if (remaining.containsKey(position) && remaining.get(position).contains(one)) {
                    List<Position> related = findTheRelatedByBox(position, one, sudokuPuzzle);

                    //build the result
                    SolutionModel solutionModel = new SolutionModel(position, one, related);
                    HintModel result = HintModel.build().of(solutionModel).of(getStrategy());

                    List<UnitModel> unitModelList = new ArrayList<>();
                    unitModelList.add(UnitModel.buildFromBox(rowStartPoint, columnStartPoint));
                    result.of(unitModelList);

                    return Optional.of(result);
                }

            }
        }

        throw new RuntimeException("should not be here.sudokuPuzzle " + sudokuPuzzle);
    }

    private List<String> findAppearOnce(Map<Position, List<String>> remaining, List<Position> positionList) {
        // key is the digital，value is the time
        Map<String, Integer> countForNumber = PuzzleTools.buildDigitalCountMap(remaining, positionList);
        // filter the time is one
        List<String> collect = countForNumber.entrySet().stream().filter(one -> one.getValue() == 1)
            .map(one -> one.getKey()).collect(Collectors.toList());

        Collections.sort(collect);
        return collect;

    }

    private List<Position> findTheRelatedByRow(Position position, String value, SudokuPuzzle sudokuPuzzle) {

        List<Position> positions = PuzzleTools.getPositionByRow(position.getRow());
        Set<Position> allRelated = new HashSet<>(positions);

        // find the empty position
        List<Position> allEmpty = positions.stream().filter(
            one -> !sudokuPuzzle.isSlotValid(one.getRow(), one.getCol()))
            .filter(one -> !one.equals(position)).collect(Collectors.toList());

        for (Position onePos : allEmpty) {

            // check the column
            Optional<Position> columnResult = sudokuPuzzle.numInColumnPosition(onePos.getCol(), value);
            if (columnResult.isPresent()) {
                allRelated.add(columnResult.get());
                continue;
            }

            //check the box
            Optional<Position> boxResult = sudokuPuzzle.numInBoxPosition(onePos.getRow(), onePos.getCol(), value);
            if (boxResult.isPresent()) {
                allRelated.add(boxResult.get());
            }
        }

        allRelated.remove(position);
        return new ArrayList<>(allRelated);
    }

    private List<Position> findTheRelatedByColumn(Position position, String value, SudokuPuzzle sudokuPuzzle) {

        // find the empty position
        List<Position> positions = PuzzleTools.getPositionByColumn(position.getCol());
        Set<Position> allRelated = new HashSet<>(positions);
        List<Position> allEmpty = positions.stream().filter(
            one -> !sudokuPuzzle.isSlotValid(one.getRow(), one.getCol()))
            .filter(one -> !one.equals(position)).collect(Collectors.toList());

        for (Position onePos : allEmpty) {
            // check the row
            Optional<Position> result = sudokuPuzzle.numInRowPosition(onePos.getRow(), value);
            if (result.isPresent()) {
                allRelated.add(result.get());
                continue;
            }

            // check the box
            Optional<Position> boxResult = sudokuPuzzle.numInBoxPosition(onePos.getRow(), onePos.getCol(), value);
            if (boxResult.isPresent()) {
                allRelated.add(boxResult.get());
            }
        }

        allRelated.remove(position);
        return new ArrayList<>(allRelated);
    }

    private List<Position> findTheRelatedByBox(Position position, String value, SudokuPuzzle sudokuPuzzle) {
        // find the empty position
        List<Position> positions = PuzzleTools.getPositionByBox(position.getRow(), position.getCol());
        Set<Position> allRelated = new HashSet<>(positions);
        List<Position> allEmpty = positions.stream().filter(
            one -> !sudokuPuzzle.isSlotValid(one.getRow(), one.getCol()))
            .filter(one -> !one.equals(position)).collect(Collectors.toList());

        for (Position onePos : allEmpty) {
            // check the row
            Optional<Position> result = sudokuPuzzle.numInRowPosition(onePos.getRow(), value);
            if (result.isPresent()) {
                allRelated.add(result.get());
                continue;
            }

            // check the column
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
    public String buildDesc(HintModel hintModel) {

        Assert.isTrue(hintModel.getUnitModelList().size() == 1, "should be 1");
        UnitModel unitModel = hintModel.getUnitModelList().get(0);
        Position position = hintModel.getSolutionModel().getPosition();
        int number = PuzzleTools.getNumber(unitModel);

        return I18nProcessor.getAppendValue(getStrategy().getName() + "_hint", number,
            I18nProcessor.getValue(unitModel.getUnit().getDesc()), hintModel.getSolutionModel().getSolutionDigital(),
            position.getDesc());

    }

}
