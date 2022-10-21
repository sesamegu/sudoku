package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:隐性数对
 *
 * @author sesame 2022/10/19
 */
public class HiddenPairsStrategy extends AbstractUnitStrategy {

    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_PAIRS;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    protected Optional<HintModel> getHintModel(Map<Position, List<String>> remaining, List<Position> positionList) {
        Map<String, Integer> countForDigital = new HashMap<>(9);
        for (Position onePosition : positionList) {
            List<String> digital = remaining.get(onePosition);
            if (CollectionUtils.isEmpty(digital)) {
                continue;
            }
            digital.forEach(oneDigital -> {
                if (countForDigital.containsKey(oneDigital)) {
                    countForDigital.put(oneDigital, countForDigital.get(oneDigital) + 1);
                } else {
                    countForDigital.put(oneDigital, 1);
                }
            });
        }

        //过滤出次数为2的数字
        List<String> collect = countForDigital.entrySet().stream().filter(entry -> entry.getValue() == 2).map(
            Entry::getKey).collect(Collectors.toList());
        if (collect.size() < 2) {
            return Optional.empty();
        }
        Collections.sort(collect);

        for (int i = 0; i <= collect.size() - 2; i++) {
            for (int j = i + 1; j <= collect.size() - 1; j++) {
                List<String> causeDigital = new ArrayList<>();
                causeDigital.add(collect.get(i));
                causeDigital.add(collect.get(j));

                boolean hasOther = false;
                List<Position> causeList = new ArrayList<>(2);
                for (Position onePosition : positionList) {
                    List<String> innerDigital = remaining.get(onePosition);
                    if (CollectionUtils.isEmpty(innerDigital) || (!innerDigital.containsAll(causeDigital))) {
                        continue;
                    }

                    causeList.add(onePosition);
                    if (innerDigital.size() > 2) {
                        hasOther = true;
                    }
                }

                if (causeList.size() == 2 && hasOther) {
                    Map<Position, List<String>> deleteMap = new HashMap<>(2);
                    List<String> deleteOne = new ArrayList<>(remaining.get(causeList.get(0)));
                    deleteOne.removeAll(causeDigital);
                    deleteMap.put(causeList.get(0), deleteOne);

                    List<String> deleteSecond = new ArrayList<>(remaining.get(causeList.get(1)));
                    deleteSecond.removeAll(causeDigital);
                    deleteMap.put(causeList.get(1), deleteSecond);

                    CandidateModel candidateModel = new CandidateModel(causeList, causeDigital, deleteMap);
                    HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                    return Optional.of(result);

                }
            }
        }

        return Optional.empty();
    }
}
