package com.sesame.game;

public enum SudokuPuzzleType {
    EASY(0.55555f,"Easy"),
    NORMAL(0.39000f,"Noraml"),
    HARD(0.22222f,"Hard");

    private final String desc;
    private final float difficult;

    SudokuPuzzleType(float difficult,String desc) {
        this.difficult = difficult;
        this.desc = desc;
    }


    public float getDifficult() {
        return difficult;
    }

    public String toString() {
        return desc;
    }
}
