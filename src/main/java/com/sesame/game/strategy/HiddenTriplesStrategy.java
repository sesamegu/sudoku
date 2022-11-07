package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.PuzzleTools;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import org.springframework.util.CollectionUtils;

/**
 * Introduction: Hidden Triples
 * When three cells in a row, column, or block contain the same three Notes. These three cells also contain other
 * candidates, which may be removed from them
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

        //find the digital which appear twice
        Map<String, Integer> countForDigital = PuzzleTools.buildDigitalCountMap(remaining, positionList);
        List<String> targetDigital = countForDigital.entrySet().stream().filter(entry -> entry.getValue() == 2).map(
            Entry::getKey).collect(Collectors.toList());
        if (targetDigital.size() < 3) {
            return Optional.empty();
        }
        Collections.sort(targetDigital);

        // iterate all combination
        for (int i = 0; i <= targetPosition.size() - 3; i++) {
            for (int j = i + 1; j <= targetPosition.size() - 2; j++) {
                for (int k = j + 1; k <= targetPosition.size() - 1; k++) {

                    Position p1 = targetPosition.get(i);
                    Position p2 = targetPosition.get(j);
                    Position p3 = targetPosition.get(k);
                    List<String> d1 = new ArrayList<>(remaining.get(p1));
                    d1.retainAll(targetDigital);
                    List<String> d2 = new ArrayList<>(remaining.get(p2));
                    d2.retainAll(targetDigital);
                    List<String> d3 = new ArrayList<>(remaining.get(p3));
                    d3.retainAll(targetDigital);

                    if (d1.size() < 2 || d2.size() < 2 || d3.size() < 2) {
                        continue;
                    }

                    Optional<HintModel> result = buildHintModel(remaining, p1, p2, p3, d1, d2, d3);
                    if (result.isPresent()) {
                        return result;
                    }

                }
            }
        }
        return Optional.empty();
    }

    /**
     * check the three positions have three pairs
     *
     * @param remaining
     * @param p1 position 1
     * @param p2 position 2
     * @param p3 position 3
     * @param d1 digital 1
     * @param d2 digital 2
     * @param d3 digital 3
     * @return
     */
    private Optional<HintModel> buildHintModel(Map<Position, List<String>> remaining, Position p1, Position p2,
        Position p3, List<String> d1, List<String> d2, List<String> d3) {
        //Position 1
        for (int na = 0; na <= d1.size() - 2; na++) {
            for (int nb = na + 1; nb <= d1.size() - 1; nb++) {
                //Position 2
                for (int ms = 0; ms <= d2.size() - 2; ms++) {
                    for (int mz = ms + 1; mz <= d2.size() - 1; mz++) {
                        //Position 3
                        for (int pd = 0; pd <= d3.size() - 2; pd++) {
                            for (int pe = pd + 1; pe <= d3.size() - 1; pe++) {
                                // get two digital from position 1
                                Map<String, Integer> innerCount = new HashMap<>();
                                List<String> nAll = new ArrayList<>();
                                nAll.add(d1.get(na));
                                nAll.add(d1.get(nb));
                                processCount(innerCount, nAll);

                                //  get two digital from position 2
                                List<String> mAll = new ArrayList<>();
                                mAll.add(d2.get(ms));
                                mAll.add(d2.get(mz));
                                processCount(innerCount, mAll);

                                //  get two digital from position 3
                                List<String> pAll = new ArrayList<>();
                                pAll.add(d3.get(pd));
                                pAll.add(d3.get(pe));
                                processCount(innerCount, pAll);

                                // three digital which appear twice
                                int count = (int)innerCount.values().stream().filter(one -> one == 2)
                                    .count();
                                if (count != 3) {
                                    continue;
                                }

                                Map<Position, List<String>> causeMap = new HashMap<>();
                                Map<Position, List<String>> deleteMap = new HashMap<>();
                                calcPosition(remaining, p1, nAll, causeMap, deleteMap);
                                calcPosition(remaining, p2, mAll, causeMap, deleteMap);
                                calcPosition(remaining, p3, pAll, causeMap, deleteMap);
                                if (CollectionUtils.isEmpty(deleteMap)) {
                                    continue;
                                }

                                CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                                HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                                return Optional.of(result);
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void calcPosition(Map<Position, List<String>> remaining, Position p2, List<String> mAll,
        Map<Position, List<String>> causeMap, Map<Position, List<String>> deleteMap) {
        List<String> innerRemain = new ArrayList<>(remaining.get(p2));
        causeMap.put(p2, mAll);
        if (innerRemain.size() > 2) {
            innerRemain.removeAll(mAll);
            deleteMap.put(p2, innerRemain);
        }
    }

    private void processCount(Map<String, Integer> innerCount, List<String> listString) {
        listString.forEach(s -> {
                if (innerCount.containsKey(s)) {
                    innerCount.put(s, innerCount.get(s) + 1);
                } else {
                    innerCount.put(s, 1);
                }
            }
        );
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_TRIPLES;
    }

}
