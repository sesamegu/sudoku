package com.sesame.game.strategy;

import java.util.List;

/**
 * Introduction: 提示的数据模型
 *
 * @author sesame 2022/10/12
 */
public class HintModel {

    //求解的位置
    private Position position;
    // 解
    private String value;
    // 相关的位置
    private List<Position> related;
    // 策略名
    private Strategy strategy;

    public static HintModel build() {
        return new HintModel();
    }

    public HintModel of(Position position) {
        this.position = position;
        return this;
    }

    public HintModel of(String value) {
        this.value = value;
        return this;
    }

    public HintModel of(List<Position> related) {
        this.related = related;
        return this;
    }

    public HintModel of(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public String getValue() {
        return value;
    }

    public List<Position> getRelated() {
        return related;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
