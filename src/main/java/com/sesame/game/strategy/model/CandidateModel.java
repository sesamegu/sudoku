package com.sesame.game.strategy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

/**
 * Introduction: candidate model
 *
 * @author sesame 2022/10/17
 */
@Getter
public class CandidateModel {
    /**
     * key is cause's position，value is the list of digital
     */
    private final Map<Position, List<String>> causeMap;

    /**
     * key is deletion's position，value is the list of digital
     */
    private final Map<Position, List<String>> deleteMap;

    public CandidateModel(List<Position> causeList, List<String> causeDigital,
        Map<Position, List<String>> deleteMap) {
        Validate.notNull(causeDigital, "should not be null");
        causeMap = new HashMap<>();
        causeList.forEach(
            one -> causeMap.put(one, new ArrayList<>(causeDigital))
        );

        this.deleteMap = deleteMap;
    }

    public CandidateModel(Map<Position, List<String>> causeMap,
        Map<Position, List<String>> deleteMap) {
        this.causeMap = causeMap;
        this.deleteMap = deleteMap;
    }

}