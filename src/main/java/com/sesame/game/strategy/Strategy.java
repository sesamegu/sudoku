package com.sesame.game.strategy;

/**
 * Introduction:
 *
 * @author sesame 2022/10/14
 */
public enum Strategy {

    LAST_FREE_CELL("唯余空白格","一个 3×3 宫、一行或一列中只剩下一个可用单元格，那么我们必须明确缺少 1 到 9 中的哪个数字，并将它填入这个空白单元格"),
    LAST_POSSIBLE_NUMBER("唯一候选数","找到缺少的数字，您应该看看感兴趣的 3×3 宫，以及与之相连的行和列中已经有哪些数字")
    ;
    private String name;
    private String desc;

    Strategy(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
