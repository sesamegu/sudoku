package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:显性三数对
 *
 * @author sesame 2022/10/19
 */
public class ObviousTriplesStrategy extends AbstractUnitStrategy {

    /**
     * 否存在显性三数对
     *
     * @param remaining
     * @param positionList
     * @return
     */
    @Override
    public Optional<HintModel> getHintModel(Map<Position, List<String>> remaining, List<Position> positionList) {
        //key 为位置，value为对应的数字

        List<Position> collect = positionList.stream().filter(
            one -> (!CollectionUtils.isEmpty(remaining.get(one))) && remaining.get(one).size() == 2).collect(
            Collectors.toList());

        if (collect.size() < 3) {
            return Optional.empty();
        }

        for (int i = 0; i <= collect.size() - 3; i++) {
            for (int j = i + 1; j <= collect.size() - 2; j++) {
                for (int k = j + 1; k <= collect.size() - 1; k++) {
                    Position p1 = collect.get(i);
                    Position p2 = collect.get(j);
                    Position p3 = collect.get(k);
                    List<Position> causeList = new ArrayList<>(3);
                    causeList.add(p1);
                    causeList.add(p2);
                    causeList.add(p3);

                    // key为数字，value为次数
                    Map<String, Integer> countForDigital = new HashMap<>();
                    for (Position onePosition : causeList) {
                        List<String> digital = remaining.get(onePosition);
                        digital.forEach(oneDigital -> {
                            if (countForDigital.containsKey(oneDigital)) {
                                countForDigital.put(oneDigital, countForDigital.get(oneDigital) + 1);
                            } else {
                                countForDigital.put(oneDigital, 1);
                            }
                        });
                    }

                    int count = (int)countForDigital.values().stream().filter(one -> one == 2).count();
                    if (count != 3) {
                        continue;
                    }

                    List<String> threeDigital = new ArrayList<>(countForDigital.keySet());
                    Collections.sort(threeDigital);
                    Assert.isTrue(threeDigital.size() == 3, "should be three.");
                    //检查其它位置是否包含任意这三个任一值
                    Map<Position, List<String>> deleteMap = new HashMap<>();
                    for (Position innerPosition : positionList) {
                        if ((!causeList.contains(innerPosition)) && (!CollectionUtils.isEmpty(
                            remaining.get(innerPosition)))) {
                            List<String> innerDigital = remaining.get(innerPosition);
                            List<String> deleteString = new ArrayList<>();
                            threeDigital.forEach(one -> {
                                if (innerDigital.contains(one)){
                                    deleteString.add(one);
                                }
                            });

                            if (!CollectionUtils.isEmpty(deleteString)) {
                                deleteMap.put(innerPosition, deleteString);
                            }
                        }
                    }

                    if (!CollectionUtils.isEmpty(deleteMap)) {
                        CandidateModel candidateModel = new CandidateModel(causeList, threeDigital, deleteMap);
                        HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                        return Optional.of(result);
                    }

                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.OBVIOUS_TRIPLES;
    }

    @Override
    public int priority() {
        return 4;
    }
}
