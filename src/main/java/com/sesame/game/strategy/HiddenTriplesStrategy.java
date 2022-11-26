package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.sesame.game.common.PuzzleTools;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.Validate;

/**
 * Introduction: Hidden Triples
 * When three cells in a row, column, or block contain the same three Notes. These three cells also contain other
 * candidates, which may be removed from them
 * four different types:
 * type 1:  2,2,2   eg: three cells candidates like [1,2] [2,3], [1,3,4]
 * type 2:  2,2,3   eg: three cells candidates like [1,2] [2,3], [1,2,3,4]
 * type 3:  2,3,3   eg: three cells candidates like [1,2] [1,2,3], [1,2,3,4]
 * type 4:  3,3,3   eg: three cells candidates like [1,2,3] [1,2,3], [1,2,3,4]
 *
 * @author sesame 2022/10/21
 */
public class HiddenTriplesStrategy extends AbstractUnitStrategy {

    @Override
    protected Optional<HintModel> processBasicUnit(Map<Position, List<String>> remaining, List<Position> positionList) {
        // find the positions which have more than 2 candidates
        List<Position> targetPosition = positionList.stream().filter(
            one -> (!CollectionUtils.isEmpty(remaining.get(one))) && remaining.get(one).size() >= 2).collect(
            Collectors.toList());

        if (targetPosition.size() < 3) {
            return Optional.empty();
        }

        //find the digital which appear twice or triple
        Map<String, List<Position>> countForDigital = PuzzleTools.buildDigPosiMap(remaining, targetPosition);
        Map<String, List<Position>> digitalMap = countForDigital.entrySet().stream().filter(
            entry -> (entry.getValue().size() == 2 || entry.getValue().size() == 3)).collect(
            Collectors.toMap(Entry::getKey, Entry::getValue));
        if (digitalMap.size() < 3) {
            return Optional.empty();
        }

        // iterate all three digital combination
        List<String> validDigital = new ArrayList<>(digitalMap.keySet());
        Collections.sort(validDigital);
        for (int i = 0; i <= validDigital.size() - 3; i++) {
            for (int j = i + 1; j <= validDigital.size() - 2; j++) {
                for (int k = j + 1; k <= validDigital.size() - 1; k++) {
                    List<String> causeDigital = new ArrayList<>();
                    causeDigital.add(validDigital.get(i));
                    causeDigital.add(validDigital.get(j));
                    causeDigital.add(validDigital.get(k));

                    // three digital only in three cells
                    Set<Position> cells = new HashSet<>();
                    causeDigital.forEach(one -> cells.addAll(digitalMap.get(one)));
                    if (cells.size() != 3) {
                        continue;
                    }
                    List<Position> causePosition = new ArrayList<>(cells);
                    Collections.sort(causePosition);

                    Optional<HintModel> result = buildHintModel(remaining, causeDigital, causePosition, digitalMap);
                    if (result.isPresent()) {
                        return result;
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Optional<HintModel> buildHintModel(Map<Position, List<String>> remaining, List<String> causeDigital,
        List<Position> causePosition, Map<String, List<Position>> digitalMap) {
        Validate.isTrue(causePosition.size() == 3, "should be three");
        Map<Position, List<String>> causeMap = new HashMap<>();
        Map<Position, List<String>> deleteMap = new HashMap<>();

        // build the cause and delete map
        for (Position position : causePosition) {
            // cause
            List<String> causeForPosition = new ArrayList<>(remaining.get(position));
            causeForPosition.retainAll(causeDigital);
            causeMap.put(position, causeForPosition);
            // delete
            List<String> deleteForPosition = new ArrayList<>(remaining.get(position));
            deleteForPosition.removeAll(causeDigital);
            if (!CollectionUtils.isEmpty(deleteForPosition)) {
                deleteMap.put(position, deleteForPosition);
            }
        }

        if (MapUtils.isEmpty(deleteMap)) {
            return Optional.empty();
        }

        CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
        HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
        // calc detail type
        DetailTypeEnum detailTypeEnum;
        int totalCount = causeMap.values().stream().mapToInt(List::size).sum();
        if (totalCount == 6) {
            detailTypeEnum = DetailTypeEnum.ONE;
        } else if (totalCount == 7) {
            detailTypeEnum = DetailTypeEnum.TWO;
        } else if (totalCount == 8) {
            detailTypeEnum = DetailTypeEnum.THREE;
        } else if (totalCount == 9) {
            detailTypeEnum = DetailTypeEnum.FOUR;
        } else {
            throw new RuntimeException("should not happen " + totalCount);
        }
        result.of(detailTypeEnum);
        return Optional.of(result);

    }

    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_TRIPLES;
    }

    @Override
    public String buildDesc(HintModel hintModel) {
        //第{0}{1}中，数字{2}、{3}、{4}只出现在位置{5}、{6}、{7}，这三个位置必然是数字{2}、{3}、{4}，删除其它候选数字

        Validate.isTrue(hintModel.getUnitModelList().size() == 1, "should be 1");

        UnitModel unitModel = hintModel.getUnitModelList().get(0);
        int number = PuzzleTools.getNumber(unitModel);

        Map<Position, List<String>> causeMap = hintModel.getCandidateModel().getCauseMap();
        List<Position> positions = new ArrayList<>(causeMap.keySet());
        Collections.sort(positions);
        Validate.isTrue(positions.size() == 3, "should be 3 ");

        Set<String> digitalSet = new HashSet<>();
        causeMap.values().forEach(one -> digitalSet.addAll(one));
        List<String> threeDigital = new ArrayList<>(digitalSet);
        Collections.sort(threeDigital);
        Validate.isTrue(threeDigital.size() == 3, "should be 3 ");

        return I18nProcessor.getAppendValue(getStrategy().getName() + "_hint",
            number,
            I18nProcessor.getValue(unitModel.getUnit().getDesc()),
            threeDigital.get(0),
            threeDigital.get(1),
            threeDigital.get(2),
            positions.get(0).getDesc(),
            positions.get(1).getDesc(),
            positions.get(2).getDesc()
        );
    }

}
