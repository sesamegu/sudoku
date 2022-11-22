package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:Obvious Pairs
 * In a the block, vertical column or horizontal row, there are two cells which have the same two candidates. Then we
 * can delete the two candidates in the area.
 *
 * @author sesame 2022/10/16
 */
public class ObviousPairsStrategy extends AbstractUnitStrategy {
    /**
     * check if exists the obvious pairs
     *
     * @param remaining
     * @param positionList
     * @return
     */
    @Override
    public Optional<HintModel> processBasicUnit(Map<Position, List<String>> remaining, List<Position> positionList) {
        //key is the digital、value the position
        Map<String, Position> contains = new HashMap<>();
        for (Position position : positionList) {
            List<String> digital = remaining.get(position);
            if (CollectionUtils.isEmpty(digital) || digital.size() != 2) {
                continue;
            }

            String key = digital.get(0) + digital.get(1);

            // two positions have the same two candidates
            if (contains.containsKey(key)) {
                List<Position> twoSamePosition = new ArrayList<>();
                twoSamePosition.add(contains.get(key));
                twoSamePosition.add(position);

                //check if other position contains the two candidates
                Map<Position, List<String>> deleteMap = new HashMap<>();
                for (Position inner : positionList) {
                    if ((!twoSamePosition.contains(inner)) && (!CollectionUtils.isEmpty(
                        remaining.get(inner)))) {
                        List<String> innerDigital = remaining.get(inner);
                        List<String> deleteString = new ArrayList<>();
                        if (innerDigital.contains(digital.get(0))) {
                            deleteString.add(digital.get(0));
                        }
                        if (innerDigital.contains(digital.get(1))) {
                            deleteString.add(digital.get(1));
                        }

                        if (!CollectionUtils.isEmpty(deleteString)) {
                            deleteMap.put(inner, deleteString);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(deleteMap)) {
                    CandidateModel candidateModel = new CandidateModel(twoSamePosition, digital, deleteMap);
                    HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                    return Optional.of(result);
                }

            } else {
                contains.put(key, position);
            }

        }
        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.OBVIOUS_PAIRS;
    }

    @Override
    public String buildDesc(HintModel hintModel) {
        return "";
    }

}
