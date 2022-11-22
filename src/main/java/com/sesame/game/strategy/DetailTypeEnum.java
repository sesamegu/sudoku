package com.sesame.game.strategy;

import lombok.Getter;

/**
 * Introduction: Detail Type Enum
 *
 * @author sesame 2022/11/22
 */

public enum DetailTypeEnum {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    ;
    @Getter
    private final int type;

    DetailTypeEnum(int type) {
        this.type = type;
    }

}
