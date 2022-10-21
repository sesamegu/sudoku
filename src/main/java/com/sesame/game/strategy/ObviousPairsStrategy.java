package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:显性数对
 *
 * @author sesame 2022/10/16
 */
public class ObviousPairsStrategy extends AbstractUnitStrategy {
    /**
     * 否存在显性数对
     *
     * @param remaining
     * @param positionList
     * @return
     */
    @Override
    public Optional<HintModel> getHintModel(Map<Position, List<String>> remaining, List<Position> positionList) {
        //key 为数字字符串、value为之前的位置
        Map<String, Position> contains = new HashMap<>();
        for (Position position : positionList) {
            List<String> digital = remaining.get(position);
            if (CollectionUtils.isEmpty(digital) || digital.size() != 2) {
                continue;
            }

            String key = digital.get(0) + digital.get(1);

            if (contains.containsKey(key)) {
                //找到两个位置包含相同的候选值
                List<Position> twoSamePosition = new ArrayList<>();
                twoSamePosition.add(contains.get(key));
                twoSamePosition.add(position);

                //检查其它位置是否包含任意这两个任一值
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
    public int priority() {
        return 4;
    }
}
