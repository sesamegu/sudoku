package com.sesame.game;

public enum SudokuPuzzleType {
    EASY(0.55555f,"Easy"),
    NORMAL(0.39000f,"Noraml"),
    HARD(0.22222f,"Hard");

    private final String[] validValues = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private final String desc;
    private final float difficult;

    SudokuPuzzleType(float difficult,String desc) {
        this.difficult = difficult;
        this.desc = desc;
    }

    public int getRows() {
        return 9;
    }

    public int getColumns() {
        return 9;
    }

    public int getBoxWidth() {
        return 3;
    }

    public int getBoxHeight() {
        return 3;
    }

    public String[] getValidValues() {
        return validValues;
    }

    public float getDifficult() {
        return difficult;
    }

    public String toString() {
        return desc;
    }
}
