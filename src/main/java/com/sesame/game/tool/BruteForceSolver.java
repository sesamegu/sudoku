package com.sesame.game.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.sesame.game.common.Const;
import com.sesame.game.common.SudokuPuzzle;

/**
 * Introduction:brute force solver
 *
 * @author sesame 2022/10/31
 */
public class BruteForceSolver {

    public static Optional<SudokuPuzzle> buildSudokuPuzzle(SudokuPuzzle puzzle) {
        Random randomGenerator = new Random();
        SudokuPuzzle copy = new SudokuPuzzle(puzzle);
        List<String> notUsedValidValues = new ArrayList<>(Arrays.asList(Const.VALID_VALUES));
        for (int r = 0; r < Const.ROWS; r++) {
            int randomValue = randomGenerator.nextInt(notUsedValidValues.size());
            copy.makeMoveWithoutCandidate(r, 0, notUsedValidValues.get(randomValue), true);
            notUsedValidValues.remove(randomValue);
        }

        boolean solved = BruteForceSolver.backtrackSudokuSolver(0, 0, copy);
        if (solved) {
            return Optional.of(copy);
        }
        return Optional.empty();
    }

    /**
     * 解决数独谜题
     * 前置条件: r = 0, c = 0
     * 后置条件: 解决后的谜题
     *
     * @param r: 当前行
     * @param c: 当前列
     * @param puzzle: 要解决的数独谜题
     * @return 是否找到有效解
     */
    public static boolean backtrackSudokuSolver(int r, int c, SudokuPuzzle puzzle) {
        // 如果到达最后一行之后，说明找到了解
        if (r >= Const.ROWS) {
            return true;
        }
        
        // 如果到达行末尾，继续下一行
        if (c >= Const.COLUMNS) {
            return backtrackSudokuSolver(r + 1, 0, puzzle);
        }

        // 检查位置是否有效
        puzzle.makeSureInRange(r, c);

        // 如果当前单元格是空的且可变的
        if (puzzle.isSlotAvailable(r, c)) {
            // 尝试所有可能的数字
            for (int i = 0; i < Const.VALID_VALUES.length; i++) {
                String value = Const.VALID_VALUES[i];
                
                // 如果当前数字在此位置有效
                if (!puzzle.numInRow(r, value) && !puzzle.numInCol(c, value) && !puzzle.numInBox(r, c, value)) {
                    // 放置数字
                    puzzle.makeMoveWithoutCandidate(r, c, value, true);

                    // 递归尝试下一个位置
                    if (backtrackSudokuSolver(r, c + 1, puzzle)) {
                        return true;
                    }

                    // 如果没有解，回溯：清空当前位置
                    puzzle.makeSlotEmptyWithoutRestCandidate(r, c);
                }
            }
            return false; // 没有找到有效数字
        } else {
            // 如果当前单元格已经有值，跳到下一个位置
            return backtrackSudokuSolver(r, c + 1, puzzle);
        }
    }
}
