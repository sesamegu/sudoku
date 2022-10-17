package com.sesame.game.strategy;

/**
 * Introduction: 所有的策略
 *
 * @author sesame 2022/10/14
 */
public enum Strategy {

    LAST_FREE_CELL("唯余空白格","一个 3×3 宫、一行或一列中只剩下一个可用单元格，那么我们必须明确缺少 1 到 9 中的哪个数字，并将它填入这个空白单元格"),
    LAST_POSSIBLE_NUMBER("唯一候选数","找到缺少的数字，您应该看看感兴趣的 3×3 宫，以及与之相连的行和列中已经有哪些数字"),
    HIDDEN_SINGLES("隐性单一数","隐性单一数的关键在于在整行、整列或 3x3 宫中对于特定的某个数字只有一个可行的位置"),
    OBVIOUS_PAIRS("显性数对","需要找到 3x3 宫中的 2 个具有相同笔记数对的单元格。这说明这些笔记数对不能用于此 3x3 宫的其他单元格中。因此，您可以将它们从笔记中删除"),
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
