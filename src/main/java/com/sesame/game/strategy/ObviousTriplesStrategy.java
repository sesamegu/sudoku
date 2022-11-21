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
import com.sesame.game.strategy.model.Position;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:Obvious Triples
 * In a block, vertical column or horizontal row， there are three cells which only contain three same candidates. Then
 * we can delete the three candidates in the area.
 * four different types:
 * type 1:  2,2,2   eg: three cells candidates like [1,2] [2,3], [1,3]
 * type 2:  2,2,3   eg: three cells candidates like [1,2] [2,3], [1,2,3]
 * type 3:  2,3,3   eg: three cells candidates like [1,2] [1,2,3], [1,2,3]
 * type 4:  3,3,3   eg: three cells candidates like [1,2,3] [1,2,3], [1,2,3]
 *
 * @author sesame 2022/10/19
 */
public class ObviousTriplesStrategy extends AbstractUnitStrategy {

    @Override
    public Optional<HintModel> processBasicUnit(Map<Position, List<String>> remaining, List<Position> positionList) {
        List<Position> collect = positionList.stream().filter(
            one -> !CollectionUtils.isEmpty(remaining.get(one)) && (remaining.get(one).size() == 2 || remaining.get(one)
                .size() == 3)).collect(Collectors.toList());

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

                    // key is digital，value is the count
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

                    // three digital
                    if (countForDigital.size() != 3) {
                        continue;
                    }

                    List<String> threeDigital = new ArrayList<>(countForDigital.keySet());
                    Collections.sort(threeDigital);
                    Assert.isTrue(threeDigital.size() == 3, "should be three.");
                    //check if other position contains the three candidates
                    Map<Position, List<String>> deleteMap = new HashMap<>();
                    for (Position innerPosition : positionList) {
                        if ((!causeList.contains(innerPosition)) && (!CollectionUtils.isEmpty(
                            remaining.get(innerPosition)))) {
                            List<String> innerDigital = remaining.get(innerPosition);
                            List<String> deleteString = new ArrayList<>();
                            threeDigital.forEach(one -> {
                                if (innerDigital.contains(one)) {
                                    deleteString.add(one);
                                }
                            });

                            if (!CollectionUtils.isEmpty(deleteString)) {
                                deleteMap.put(innerPosition, deleteString);
                            }
                        }
                    }

                    if (!CollectionUtils.isEmpty(deleteMap)) {
                        Map<Position, List<String>> causeMap = new HashMap<>(3);
                        causeList.forEach(
                            onePosition -> causeMap.put(onePosition, remaining.get(onePosition))
                        );
                        CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
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

}
