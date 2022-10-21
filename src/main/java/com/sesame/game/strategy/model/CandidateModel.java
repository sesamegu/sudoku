package com.sesame.game.strategy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sesame.game.strategy.Position;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * Introduction:
 *
 * @author sesame 2022/10/17
 */
@Getter
public class CandidateModel {
    /**
     * 原因位置及对应的数字：key为位置，value为对应的值
     */
    private Map<Position, List<String>> causeMap;

    /**
     * 删除的位置及对应的值：key为位置，value为对应的值
     */
    private Map<Position, List<String>> deleteMap;

    public CandidateModel(List<Position> causeList, List<String> causeDigital,
        Map<Position, List<String>> deleteMap) {
        Assert.notNull(causeDigital, "should not be null");
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