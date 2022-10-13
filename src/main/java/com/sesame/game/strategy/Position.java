package com.sesame.game.strategy;

/**
 * Introduction:
 *
 * @author sesame 2022/10/13
 */
public
class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Position) {
            Position another = (Position)anObject;

            return (this.row == another.row && this.col == another.col);

        }
        return false;
    }

}