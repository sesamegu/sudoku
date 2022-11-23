package com.sesame.game.strategy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sesame.game.strategy.DetailTypeEnum;
import com.sesame.game.strategy.Strategy;
import lombok.Getter;

/**
 * Introduction: hint model
 *
 * @author sesame 2022/10/12
 */
@Getter
public class HintModel {

    private Strategy strategy;
    private boolean isCandidateModel;
    private SolutionModel solutionModel;
    private CandidateModel candidateModel;
    private List<UnitModel> unitModelList;
    private DetailTypeEnum detailTypeEnum;
    /**
     * for temp data passing
     */
    private Map<Object,Object> tempData = new HashMap<>();

    public static HintModel build() {
        return new HintModel();
    }

    public HintModel of(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public HintModel of(SolutionModel solutionModel) {
        this.isCandidateModel = false;
        this.solutionModel = solutionModel;
        return this;
    }

    public HintModel of(CandidateModel candidateModel) {
        this.isCandidateModel = true;
        this.candidateModel = candidateModel;
        return this;
    }

    public HintModel of(List<UnitModel> unitModelList) {
        this.unitModelList = unitModelList;
        return this;
    }

    public HintModel of(DetailTypeEnum detailTypeEnum) {
        this.detailTypeEnum = detailTypeEnum;
        return this;
    }

}

