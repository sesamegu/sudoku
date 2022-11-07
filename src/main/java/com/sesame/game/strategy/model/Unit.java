package com.sesame.game.strategy.model;

import lombok.Getter;

/**
 * Introduction: the three basic units
 *
 * @author sesame 2022/10/27
 */
public enum Unit {
    ROW("row "),
    COLUMN("column"),
    BOX("box");

    @Getter
    private final String desc;

    Unit(String desc) {
        this.desc = desc;
    }

}
