package com.sesame.game.strategy;

import java.util.Objects;

/**
 * Introduction:
 *
 * @author sesame 2022/10/13
 */
public class Position implements Comparable<Position> {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position)o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public int compareTo(Position o) {
        if (row > o.row) {
            return 1;
        } else if (row < o.row) {
            return -1;
        } else {
            if (col > o.col) {
                return 1;
            }else if (col<o.col){
                return -1;
            }else {
                return 0;
            }
        }
    }
}