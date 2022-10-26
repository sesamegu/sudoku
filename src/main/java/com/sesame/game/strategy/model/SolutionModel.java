package com.sesame.game.strategy.model;

import java.util.List;

import lombok.Getter;

/**
 * Introduction: 解的模型
 *
 * @author sesame 2022/10/19
 */
@Getter
public class SolutionModel {
    /**
     * 求解的位置
     */
    private Position position;
    /**
     * 解
     */
    private String value;
    /**
     * 相关的位置
     */
    private List<Position> related;

    public SolutionModel(Position position, String value, List<Position> related) {
        this.position = position;
        this.value = value;
        this.related = related;
    }
}

