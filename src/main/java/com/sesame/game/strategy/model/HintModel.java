package com.sesame.game.strategy.model;

import java.util.List;

import com.sesame.game.strategy.Position;
import com.sesame.game.strategy.Strategy;
import lombok.Getter;

/**
 * Introduction: 提示的数据模型
 *
 * @author sesame 2022/10/12
 */
@Getter
public class HintModel {

    /**
     * 策略名
     */
    private Strategy strategy;
    /**
     * 是否候选数模式
     */
    private boolean isCandidate;

    /**
     * 解的模型
     */
    private SolutionModel solutionModel;

    /**
     * 候选数模型
     */
    private CandidateModel candidateModel;

    public static HintModel build() {
        return new HintModel();
    }

    public HintModel of(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public HintModel of (SolutionModel solutionModel ){
        this.isCandidate = false;
        this.solutionModel = solutionModel;
        return this;
    }
    public HintModel of(CandidateModel candidateModel) {
        this.isCandidate = true;
        this.candidateModel = candidateModel;
        return this;
    }

}

