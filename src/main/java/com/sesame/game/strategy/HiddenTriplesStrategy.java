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
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

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
        Assert.isTrue(causePosition.size() == 3, "should be three");
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

        if (CollectionUtils.isEmpty(deleteMap)) {
            return Optional.empty();
        }

        CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
        HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
        return Optional.of(result);

    }
    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_TRIPLES;
    }

}
