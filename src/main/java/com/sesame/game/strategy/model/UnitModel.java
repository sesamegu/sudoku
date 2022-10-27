package com.sesame.game.strategy.model;

import lombok.Getter;

/**
 * Introduction: Unit模型
 *
 * @author sesame 2022/10/27
 */
public class UnitModel {

    @Getter
    private Unit unit = null;
    @Getter
    private int row = -1;
    @Getter
    private int column = -1;

    public static UnitModel buildFromRow(int row) {
        UnitModel unitModel = new UnitModel();
        unitModel.unit = Unit.ROW;
        unitModel.row = row;
        return unitModel;
    }

    public static UnitModel buildFromColumn(int column) {
        UnitModel unitModel = new UnitModel();
        unitModel.unit = Unit.COLUMN;
        unitModel.column = column;
        return unitModel;
    }

    public static UnitModel buildFromBox(int row, int column) {
        UnitModel unitModel = new UnitModel();
        unitModel.unit = Unit.BOX;
        unitModel.row = row;
        unitModel.column = column;
        return unitModel;
    }

}
