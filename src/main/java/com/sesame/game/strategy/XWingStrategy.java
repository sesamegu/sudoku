package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
 * Introduction: X Wing
 * Find the four cells which is a X patten. The four cells have a candidate which doesn't belong to the
 * other cell in the row or column. Then we can delete the  candidate in the column or row.
 *
 * @author sesame 2022/10/22
 */
public class XWingStrategy implements FillStrategy {
    @Override
    public Optional<HintModel> execute(SudokuPuzzle sudokuPuzzle) {
        Map<Position, List<String>> remaining = sudokuPuzzle.findRemaining();

        // by row, find the digital as the candidate appear twice
        List<Map<String, List<Position>>> allList = new ArrayList<>();
        for (int i = 0; i < Const.ROWS; i++) {
            List<Position> rowList = PuzzleTools.getPositionByRow(i);
            Map<String, List<Position>> collect = countDigital(rowList, remaining);
            if (!CollectionUtils.isEmpty(collect)) {
                allList.add(collect);
            }
        }

        if (allList.size() >= 2) {
            Optional<HintModel> result = buildHintModel(remaining, allList, Direction.ROW);
            if (result.isPresent()) {
                return result;
            }
        }

        // by column, find the digital as the candidate appear twice
        allList = new ArrayList<>();
        for (int i = 0; i < Const.ROWS; i++) {
            List<Position> columnList = PuzzleTools.getPositionByColumn(i);
            Map<String, List<Position>> collect = countDigital(columnList, remaining);
            if (!CollectionUtils.isEmpty(collect)) {
                allList.add(collect);
            }
        }

        if (allList.size() >= 2) {
            Optional<HintModel> result = buildHintModel(remaining, allList, Direction.COLUMN);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    private Optional<HintModel> buildHintModel(Map<Position, List<String>> remaining,
        List<Map<String, List<Position>>> allList, Direction direction) {
        // double iterate, find the digital which are the same position in two rows  or two columns
        for (int i = 0; i < allList.size() - 1; i++) {
            Map<String, List<Position>> outter = allList.get(i);
            for (int j = i + 1; j < allList.size(); j++) {
                Map<String, List<Position>> inner = allList.get(j);
                for (Entry<String, List<Position>> oneEntry : outter.entrySet()) {
                    String outterDigital = oneEntry.getKey();
                    if (!inner.containsKey(outterDigital)) {
                        continue;
                    }
                    List<Position> innerList = inner.get(outterDigital);
                    Collections.sort(innerList);
                    List<Position> outList = oneEntry.getValue();
                    Collections.sort(outList);
                    Assert.isTrue(innerList.size() == outList.size(), "should be same");
                    Assert.isTrue(innerList.size() == 2, "should be two");

                    //X Wing
                    boolean isXWing;
                    if (direction == Direction.ROW) {
                        isXWing = innerList.get(0).getCol() == outList.get(0).getCol() &&
                            innerList.get(1).getCol() == outList.get(1).getCol();
                    } else {
                        isXWing = innerList.get(0).getRow() == outList.get(0).getRow() &&
                            innerList.get(1).getRow() == outList.get(1).getRow();
                    }
                    if (isXWing) {
                        Map<Position, List<String>> deleteMap = new HashMap<>();

                        List<Position> relatedList;
                        if (direction == Direction.ROW) {
                            //find the candidate appear in the two columns
                            relatedList = PuzzleTools.getPositionByColumn(innerList.get(0).getCol());
                            relatedList.addAll(PuzzleTools.getPositionByColumn(innerList.get(1).getCol()));
                        } else {
                            //find the candidate appear in the two rows
                            relatedList = PuzzleTools.getPositionByRow(innerList.get(0).getRow());
                            relatedList.addAll(PuzzleTools.getPositionByRow(innerList.get(1).getRow()));
                        }

                        for (Position position : relatedList) {
                            if (innerList.contains(position) || outList.contains(position)) {
                                continue;
                            }

                            List<String> strings = remaining.get(position);
                            if (CollectionUtils.isEmpty(strings)) {
                                continue;
                            }

                            if (strings.contains(outterDigital)) {
                                List<String> digital = new ArrayList<>();
                                digital.add(outterDigital);
                                deleteMap.put(position, digital);
                            }
                        }

                        //build the result
                        if (!CollectionUtils.isEmpty(deleteMap)) {
                            Map<Position, List<String>> causeMap = new HashMap<>();
                            List<String> digital = new ArrayList<>();
                            digital.add(outterDigital);
                            causeMap.put(outList.get(0), digital);

                            digital = new ArrayList<>();
                            digital.add(outterDigital);
                            causeMap.put(outList.get(1), digital);

                            digital = new ArrayList<>();
                            digital.add(outterDigital);
                            causeMap.put(innerList.get(0), digital);

                            digital = new ArrayList<>();
                            digital.add(outterDigital);
                            causeMap.put(innerList.get(1), digital);

                            CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                            HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);

                            if (direction == Direction.ROW) {
                                List<UnitModel> unitModelList = new ArrayList<>();
                                unitModelList.add(UnitModel.buildFromRow(outList.get(0).getRow()));
                                unitModelList.add(UnitModel.buildFromRow(innerList.get(0).getRow()));
                                result.of(unitModelList);
                            } else {
                                List<UnitModel> unitModelList = new ArrayList<>();
                                unitModelList.add(UnitModel.buildFromColumn(outList.get(0).getCol()));
                                unitModelList.add(UnitModel.buildFromColumn(innerList.get(0).getCol()));
                                result.of(unitModelList);
                            }

                            return Optional.of(result);
                        }
                    }
                }

            }
        }
        return Optional.empty();
    }

    /**
     * find the digital as the candidate appear twice
     */
    private Map<String, List<Position>> countDigital(List<Position> positionList,
        Map<Position, List<String>> remaining) {
        Map<String, List<Position>> countPerDigital = new HashMap<>();
        for (Position onePosition : positionList) {
            List<String> digitalList = remaining.get(onePosition);
            if (CollectionUtils.isEmpty(digitalList)) {
                continue;
            }

            for (String oneDigital : digitalList) {
                if (countPerDigital.containsKey(oneDigital)) {
                    countPerDigital.get(oneDigital).add(onePosition);
                } else {
                    List<Position> value = new ArrayList<>();
                    value.add(onePosition);
                    countPerDigital.put(oneDigital, value);
                }
            }
        }
        return countPerDigital.entrySet().stream().filter(one -> one.getValue().size() == 2).collect(
            Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

    }

    @Override
    public Strategy getStrategy() {
        return Strategy.X_WING;
    }

}
