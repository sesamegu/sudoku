package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:Hidden Three
 * In a block, vertical column or horizontal row， there are three cells which have the same three candidates. Then we
 * can delete the three candidates in the area.
 *
 * @author sesame 2022/10/24
 */
public class HiddenThreeStrategy extends AbstractUnitStrategy {
    @Override
    protected Optional<HintModel> processBasicUnit(Map<Position, List<String>> remaining, List<Position> positionList) {

        // find the positions which have three candidates
        List<Position> filterList = positionList.stream().filter(
            one -> !CollectionUtils.isEmpty(remaining.get(one)) && remaining.get(one).size() == 3).collect(
            Collectors.toList());
        int size = filterList.size();
        if (size < 3) {
            return Optional.empty();
        }

        for (int i = 0; i < size - 2; i++) {
            for (int j = i + 1; j < size - 1; j++) {
                for (int k = j + 1; k < size; k++) {

                    Position first = filterList.get(i);
                    Position second = filterList.get(j);
                    Position third = filterList.get(k);
                    //three cells have the same candidates
                    List<String> firstRemain = remaining.get(first);
                    if (firstRemain.equals(remaining.get(second)) && firstRemain.equals(
                        remaining.get(third))) {
                        Map<Position, List<String>> deleteMap = new HashMap<>();
                        //find the related position
                        for (Position onePosition : positionList) {
                            //filter themselves
                            if (first.equals(onePosition) || second.equals(onePosition) || third.equals(onePosition)) {
                                continue;
                            }

                            if (CollectionUtils.isEmpty(remaining.get(onePosition))) {
                                continue;
                            }

                            List<String> currentRemain = new ArrayList<>(remaining.get(onePosition));
                            currentRemain.retainAll(firstRemain);
                            if (!CollectionUtils.isEmpty(currentRemain)) {
                                deleteMap.put(onePosition, currentRemain);
                            }
                        }

                        if (!CollectionUtils.isEmpty(deleteMap)) {
                            Map<Position, List<String>> causeMap = new HashMap<>();
                            causeMap.put(first, new ArrayList<>(remaining.get(first)));
                            causeMap.put(second, new ArrayList<>(remaining.get(second)));
                            causeMap.put(third, new ArrayList<>(remaining.get(third)));

                            CandidateModel candidateModel = new CandidateModel(causeMap, deleteMap);
                            HintModel result = HintModel.build().of(getStrategy()).of(candidateModel);
                            return Optional.of(result);
                        }
                    }
                }
            }

        }

        return Optional.empty();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_THREE;
    }

}
