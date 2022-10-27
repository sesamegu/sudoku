package com.sesame.game.strategy.model;

import lombok.Getter;

/**
 * Introduction: 基本单元
 *
 * @author sesame 2022/10/27
 */
public enum Unit {
    ROW("row "),
    COLUMN("column"),
    BOX("box");

    @Getter
    private String desc;

    Unit(String desc) {
        this.desc = desc;
    }

}
