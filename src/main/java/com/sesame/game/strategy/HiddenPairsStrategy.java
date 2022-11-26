package com.sesame.game.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sesame.game.common.PuzzleTools;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.UnitModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

/**
 * Introduction: Hidden Pairs
 * If you can find two cells within a row, column, or block where two Notes appear nowhere outside these cells, these
 * two Notes must be placed in the two cells. All other Notes can be eliminated from these two cells.
 *
 * @author sesame 2022/10/19
 */
public class HiddenPairsStrategy extends AbstractUnitStrategy {

    @Override
    public Strategy getStrategy() {
        return Strategy.HIDDEN_PAIRS;
    }

    @Override
    protected Optional<HintModel> processBasicUnit(Map<Position, List<String>> remaining, List<Position> positionList) {
        Map<String, Integer> countForDigital = PuzzleTools.buildDigitalCountMap(remaining, positionList);

        // find the digital which are appeared twice.
        List<String> collect = countForDigital.entrySet().stream().filter(entry -> entry.getValue() == 2).map(
            Entry::getKey).collect(Collectors.toList());
        if (collect.size() < 2) {
            return Optional.empty();
        }
        Collections.sort(collect);

        // iterate the digital's comb
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
                // check the there are two positions and one or two position' candidate number is bigger than 2
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

    @Override
    public String buildDesc(HintModel hintModel) {
        //第{0}{1}中，数字{2}、{3}只出现在位置{4}、{5}，这两个位置必然是数字{2}、{3}，删除其它候选数字
        Validate.isTrue(hintModel.getUnitModelList().size() == 1, "should be 1");

        UnitModel unitModel = hintModel.getUnitModelList().get(0);
        int number = PuzzleTools.getNumber(unitModel);

        Map<Position, List<String>> causeMap = hintModel.getCandidateModel().getCauseMap();
        List<Position> positions = new ArrayList<>(causeMap.keySet());
        Collections.sort(positions);
        Validate.isTrue(positions.size() == 2, "should be 2 ");

        List<String> twoDigital = new ArrayList<>(causeMap.values().iterator().next());
        Collections.sort(twoDigital);
        Validate.isTrue(twoDigital.size() == 2, "should be 2 ");

        return I18nProcessor.getAppendValue(getStrategy().getName() + "_hint",
            number,
            I18nProcessor.getValue(unitModel.getUnit().getDesc()),
            twoDigital.get(0),
            twoDigital.get(1),
            positions.get(0).getDesc(),
            positions.get(1).getDesc()
        );

    }

}
