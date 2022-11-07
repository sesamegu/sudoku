package com.sesame.game.strategy.model;

import java.util.List;

import lombok.Getter;

/**
 * Introduction: solution model
 *
 * @author sesame 2022/10/19
 */
@Getter
public class SolutionModel {

    private final Position position;

    private final String solutionDigital;

    private final List<Position> related;

    public SolutionModel(Position position, String value, List<Position> related) {
        this.position = position;
        this.solutionDigital = value;
        this.related = related;
    }
}

