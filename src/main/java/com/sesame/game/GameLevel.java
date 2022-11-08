package com.sesame.game;

public enum GameLevel {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard"),
    VIP("VIP");

    private final String desc;

    GameLevel(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return desc;
    }
}
