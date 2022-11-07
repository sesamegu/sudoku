package com.sesame.game;

public enum GameLevel {
    EASY(0.55555f, "Easy"),
    NORMAL(0.45000f, "Noraml"),
    HARD(0.22222f, "Hard"),
    VIP(0.6666f, "VIP");

    private final String desc;
    private final float difficult;

    GameLevel(float difficult, String desc) {
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
