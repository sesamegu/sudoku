package com.sesame.game.strategy.model;

import java.util.List;
import java.util.Map;

import com.sesame.game.strategy.Position;
import lombok.Getter;

/**
 * Introduction:
 *
 * @author sesame 2022/10/17
 */
@Getter
public class CandidateModel {

    /**
     * 规则的原因位置
     */
    private List<Position> causeList;
    /**
     * 规则数字
     */
    private List<String> causeDigital;
    /**
     * 删除的位置及对应的值：key为，value为对应的值
     */
    private Map<Position, List<String>> deleteMap;

    public CandidateModel(List<Position> causeList, List<String> causeDigital,
        Map<Position, List<String>> deleteMap) {
        this.causeList = causeList;
        this.causeDigital = causeDigital;
        this.deleteMap = deleteMap;
    }
}