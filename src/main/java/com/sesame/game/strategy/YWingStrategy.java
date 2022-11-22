package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.Direction;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:Y Wing or XY Wing
 * "Y-Wing" technique is similar to "X-Wing", but it based on three corners instead of four.
 * two pattern: by row (2 types), by box(4 types)
 *
 * @author sesame 2022/10/23
 */
public class YWingStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();
        // process by row
        Optional<HintModel> result = processRowHintModel(remaining);
        if (result.isPresent()) {
            return result;
        }

        //process by box, iterate the nine boxes
        for (int row = 0; row < Const.ROWS; row = row + Const.BOX_WIDTH) {
            for (int column = 0; column < Const.COLUMNS; column = column + Const.BOX_WIDTH) {

                // find the position which have two candidates
                List<Position> boxList = PuzzleTools.getPositionByBox(row, column);
                List<Position> filterList = boxList.stream().filter(one -> (!CollectionUtils.isEmpty(
                    remaining.get(one))) && remaining.get(one).size() == 2).collect(Collectors.toList());
                int size = filterList.size();
                if (size < 2) {
                    continue;
                }

                Optional<HintModel> hintModel = processBoxHintModel(remaining, filterList, row, column);
                if (hintModel.isPresent()) {
                    Assert.notNull(hintModel.get().getUnitModelList(), "should not be null");
                    hintModel.get().getUnitModelList().add(UnitModel.buildFromBox(row, column));
                    return hintModel;
                }

            }
        }
        return Optional.empty();

    }

    private Optional<HintModel> processBoxHintModel(Map<Position, List<String>> remaining,
        List<Position> filterList, int rowStart, int columnStart) {
        int size = filterList.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                Position first = filterList.get(i);
                Position second = filterList.get(j);
                // not the same row and same column
                if (first.getCol() == second.getCol() || first.getRow() == second.getRow()) {
                    continue;
                }

                //two candidates: one is the same, the other is different
                List<String> sameDigital = new ArrayList<>(remaining.get(first));
                sameDigital.retainAll(remaining.get(second));
                if (sameDigital.size() != 1) {
                    continue;
                }

                //build the two digital in the third cell
                List<String> thirdDigital = new ArrayList<>();
                thirdDigital.addAll(remaining.get(first));
                thirdDigital.addAll(remaining.get(second));
                thirdDigital.removeAll(sameDigital);
                Assert.isTrue(thirdDigital.size() == 2, "should be two");
                Collections.sort(thirdDigital);

                // type 1: by the first column
                List<String> tempDigital = new ArrayList<>(thirdDigital);
                tempDigital.removeAll(remaining.get(first));
                Assert.isTrue(tempDigital.size() == 1, "should be one");
                String firstDeleteDigital = tempDigital.get(0);

                List<Position> positionByColumn = PuzzleTools.getPositionByColumn(first.getCol());
                Optional<HintModel> hintModel = boxBuildHintModel(remaining, rowStart, columnStart, first, second,
                    thirdDigital, positionByColumn, firstDeleteDigital, Direction.COLUMN, second.getCol(), -1);
                if (hintModel.isPresent()) {
                    List<UnitModel> unitModelList = new ArrayList<>();
                    unitModelList.add(UnitModel.buildFromColumn(first.getCol()));
                    hintModel.get().of(unitModelList);
                    return hintModel;
                }

                //  type 2: by the second column
                tempDigital = new ArrayList<>(thirdDigital);
                tempDigital.removeAll(remaining.get(second));
                Assert.isTrue(tempDigital.size() == 1, "should be one");
                String secondDeleteDigital = tempDigital.get(0);
                positionByColumn = PuzzleTools.getPositionByColumn(second.getCol());
                hintModel = boxBuildHintModel(remaining, rowStart, columnStart, first, second,
                    thirdDigital, positionByColumn, secondDeleteDigital, Direction.COLUMN, first.getCol(), -1);
                if (hintModel.isPresent()) {
                    List<UnitModel> unitModelList = new ArrayList<>();
                    unitModelList.add(UnitModel.buildFromColumn(second.getCol()));
                    hintModel.get().of(unitModelList);
                    return hintModel;
                }

                //  type 3: by the first row
                List<Position> positionByRow = PuzzleTools.getPositionByRow(first.getRow());
                hintModel = boxBuildHintModel(remaining, rowStart, columnStart, first, second,
                    thirdDigital, positionByRow, firstDeleteDigital, Direction.ROW, -1, second.getRow());
                if (hintModel.isPresent()) {
                    List<UnitModel> unitModelList = new ArrayList<>();
                    unitModelList.add(UnitModel.buildFromRow(first.getRow()));
                    hintModel.get().of(unitModelList);
                    return hintModel;
                }

                // type 3: by the second row
                positionByRow = PuzzleTools.getPositionByRow(second.getRow());
                hintModel = boxBuildHintModel(remaining, rowStart, columnStart, first, second,
                    thirdDigital, positionByRow, secondDeleteDigital, Direction.ROW, -1, first.getRow());
                if (hintModel.isPresent()) {
                    List<UnitModel> unitModelList = new ArrayList<>();
                    unitModelList.add(UnitModel.buildFromRow(second.getRow()));
                    hintModel.get().of(unitModelList);
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
            //filter the same box
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

            //build the Y wing positions
            List<Position> possiblePosition = new ArrayList<>();
            if (direction == Direction.COLUMN) {
                // the forth cell potential five positions:
                int thirdCellBoxRowStart = third.getRow() - third.getRow() % Const.BOX_HEIGHT;
                possiblePosition.add(new Position(thirdCellBoxRowStart, forthColumn));
                possiblePosition.add(new Position(thirdCellBoxRowStart + 1, forthColumn));
                possiblePosition.add(new Position(thirdCellBoxRowStart + 2, forthColumn));

                List<Position> threeColumnInBox = new ArrayList<>(3);
                threeColumnInBox.add(new Position(row, third.getCol()));
                threeColumnInBox.add(new Position(row + 1, third.getCol()));
                threeColumnInBox.add(new Position(row + 2, third.getCol()));
                //remove themselves
                threeColumnInBox.remove(first);
                threeColumnInBox.remove(second);

                Assert.isTrue(threeColumnInBox.size() == 2, "should be two");
                possiblePosition.addAll(threeColumnInBox);

            } else {
                // the forth cell potential five positions
                int thirdCellBoxColumnStart = third.getCol() - third.getCol() % Const.BOX_WIDTH;
                possiblePosition.add(new Position(forthRow, thirdCellBoxColumnStart));
                possiblePosition.add(new Position(forthRow, thirdCellBoxColumnStart + 1));
                possiblePosition.add(new Position(forthRow, thirdCellBoxColumnStart + 2));

                List<Position> threeColumnInBox = new ArrayList<>(3);
                threeColumnInBox.add(new Position(third.getRow(), column));
                threeColumnInBox.add(new Position(third.getRow(), column + 1));
                threeColumnInBox.add(new Position(third.getRow(), column + 2));
                //remove themselves
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

    private Optional<HintModel> processRowHintModel(Map<Position, List<String>> remaining) {
        // by row, find the positions which have two candidates
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

        // by column, find the positions which have two candidates
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

        // double iterate the rows
        for (int i = 0; i < allRowList.size(); i++) {
            List<Position> positionList = allRowList.get(i);
            if (positionList.size() < 2) {
                continue;
            }
            for (int n = 0; n < positionList.size() - 1; n++) {
                for (int m = n + 1; m < positionList.size(); m++) {

                    Position first = positionList.get(n);
                    Position second = positionList.get(m);
                    // find two position which have one common digital
                    List<String> causeDigital = new ArrayList<>(remaining.get(first));
                    causeDigital.retainAll(remaining.get(second));
                    if (causeDigital.size() != 1) {
                        continue;
                    }
                    //define the candidates which are the third cell needed
                    List<String> thirdDigital = new ArrayList<>();
                    thirdDigital.addAll(remaining.get(first));
                    thirdDigital.addAll(remaining.get(second));
                    thirdDigital.removeAll(causeDigital);
                    Assert.isTrue(thirdDigital.size() == 2, "should be two");
                    Collections.sort(thirdDigital);

                    // type 1: find the right column
                    List<String> tempDigital = new ArrayList<>(thirdDigital);
                    tempDigital.removeAll(remaining.get(first));
                    Assert.isTrue(tempDigital.size() == 1, "should be one");
                    String deleteDigital = tempDigital.get(0);

                    Optional<HintModel> result = buildHintModel(remaining, first, second,
                        thirdDigital, deleteDigital, first.getCol(), second.getCol());
                    if (result.isPresent()) {
                        List<UnitModel> unitModelList = new ArrayList<>();
                        unitModelList.add(UnitModel.buildFromRow(first.getRow()));
                        unitModelList.add(UnitModel.buildFromColumn(first.getCol()));
                        result.get().of(unitModelList);
                        return result;
                    }

                    // type 2: find the left column
                    tempDigital = new ArrayList<>(thirdDigital);
                    tempDigital.removeAll(remaining.get(second));
                    Assert.isTrue(tempDigital.size() == 1, "should be one");
                    deleteDigital = tempDigital.get(0);

                    result = buildHintModel(remaining, first, second,
                        thirdDigital, deleteDigital, second.getCol(), first.getCol());
                    if (result.isPresent()) {
                        List<UnitModel> unitModelList = new ArrayList<>();
                        unitModelList.add(UnitModel.buildFromRow(second.getRow()));
                        unitModelList.add(UnitModel.buildFromColumn(second.getCol()));
                        result.get().of(unitModelList);
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
            // check the third cell and check the forth cell include the candidate
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

}
