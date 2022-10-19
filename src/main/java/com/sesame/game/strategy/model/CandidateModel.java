package com.sesame.game.strategy.model;

import java.util.List;

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
     * 候选数字
     */
    private List<String> digitalString;
    /**
     * 相关的位置
     */
    private List<Position> relatedList;

    public CandidateModel(List<Position> causeList, List<String> digitalString,
        List<Position> relatedList) {
        this.causeList = causeList;
        this.digitalString = digitalString;
        this.relatedList = relatedList;
    }
}