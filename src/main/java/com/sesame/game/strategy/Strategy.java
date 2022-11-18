package com.sesame.game.strategy;

/**
 * Introduction: all strategies
 *
 * @author sesame 2022/10/14
 */
public enum Strategy {

    LAST_FREE_CELL("last_free_cell"),
    LAST_POSSIBLE_NUMBER("last_possible_number"),
    HIDDEN_SINGLES("hidden_singles"),
    OBVIOUS_PAIRS("obvious_pairs"),
    OBVIOUS_TRIPLES("obvious_triples"),
    HIDDEN_PAIRS("hidden_pairs"),
    HIDDEN_TRIPLES("hidden_triples"),
    POINTING_PAIRS("pointing_pairs"),
    ROW_COLUMN_TO_BOX("row_column_to_box"),
    X_WING("x_wing"),
    Y_WING("y_wing"),
    SWORDFISH("swordfish"),
    HIDDEN_THREE("hidden_three"),
    ;

    private final String name;

    Strategy(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
