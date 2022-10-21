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
    OBVIOUS_TRIPLES("显性三数对","这种数独解法建立在之前的“显性数对”基础之上。但“显性三数组”不是基于笔记中的两个数字，而是基于三个数字。这是唯一的区别。"),
    HIDDEN_PAIRS("隐形数对","如果您发现在一行、一列或一个 3x3 宫的两个单元格中包含两条未出现在其他地方的笔记，那么这两条笔记必须放在这两个单元格中。所有其他笔记都可以从这两个单元格中删除。"),
    HIDDEN_TRIPLES("隐形三数对","当一行、一列或一个 3x3 宫中的三个单元格包含三条相同的笔记时，“隐性三数组”适用。这三个单元格还包含其他候选数，可以将它们删除。"),
    POINTING_PAIRS("宫区块数对","当一条笔记在一个宫中出现两次并且该笔记属于同一行或列时，“宫区块数对”适用。这说明该笔记必然是这个宫中这两个单元格之一的解。因此，您可以将该笔记从这一行或列的任何其他单元格中删除。"),
    POINTING_TRIPLES("宫区块三数对","如果一条笔记只出现在一个 3x3 宫的三个单元格中并且属于同一行或同一列，“宫区块三数组”适用。这说明该笔记必然是这个宫中这三个单元格之一的解。因此，显然它不能是这一行或这一列的任何其他单元格的解，可以将其排除"),

    ;//todo 三三三显性规则
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
