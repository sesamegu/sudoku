package com.sesame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.sesame.game.strategy.model.Position;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

/**
 * 底层数据结构
 *
 * @author mike
 */
public class SudokuPuzzle {

    @Getter
    protected String[][] board;
    /**
     * Table to determine if a slot is mutable
     */
    protected boolean[][] mutable;

    /**
     * Table to store the candidate
     */
    protected List<String>[][] candidate;

    public SudokuPuzzle() {
        this.board = new String[Const.ROWS][Const.COLUMNS];
        this.mutable = new boolean[Const.ROWS][Const.COLUMNS];
        this.candidate = new ArrayList[Const.ROWS][Const.COLUMNS];
        initializeBoard();
        initializeMutableSlots();
    }

    public SudokuPuzzle(SudokuPuzzle puzzle) {
        this.board = new String[Const.ROWS][Const.COLUMNS];
        for (int r = 0; r < Const.ROWS; r++) {
            for (int c = 0; c < Const.COLUMNS; c++) {
                board[r][c] = puzzle.board[r][c];
            }
        }
        this.mutable = new boolean[Const.ROWS][Const.COLUMNS];
        for (int r = 0; r < Const.ROWS; r++) {
            for (int c = 0; c < Const.COLUMNS; c++) {
                this.mutable[r][c] = puzzle.mutable[r][c];
            }
        }
        this.candidate = new ArrayList[Const.ROWS][Const.COLUMNS];
        resetCandidate();
    }

    public void makeMove(int row, int col, String value, boolean isMutable) {
        if (this.isValidValue(value) && this.isValidMove(row, col, value) && this.isSlotMutable(row, col)) {
            this.board[row][col] = value;
            this.mutable[row][col] = isMutable;

            //删除本位置的所有候选数
            candidate[row][col] = null;
            //删除对应的行的候选数中包含本数值的数
            for (int i = 0; i < Const.ROWS; i++) {
                if (CollectionUtils.isEmpty(candidate[i][col])) {
                    continue;
                }
                candidate[i][col].remove(value);
            }
            //删除对应的列的候选数中包含本数值的数
            for (int i = 0; i < Const.COLUMNS; i++) {
                if (CollectionUtils.isEmpty(candidate[row][i])) {
                    continue;
                }
                candidate[row][i].remove(value);
            }
            //删除对应的宫的候选数中包含本数值的数
            int rowStart = row - row % Const.BOX_WIDTH;
            int columnStart = col - col % Const.BOX_WIDTH;
            for (int i = rowStart; i < rowStart + Const.BOX_WIDTH; i++) {
                for (int j = columnStart; j < columnStart + Const.BOX_WIDTH; j++) {
                    if (CollectionUtils.isEmpty(candidate[i][j])) {
                        continue;
                    }
                    candidate[i][j].remove(value);
                }
            }
        }
    }

    public void deleteCandidate(int row, int col, List<String> deleteStr) {
        List<String> strings = candidate[row][col];
        strings.removeAll(deleteStr);
    }

    public void setCandidate(int row, int col, List<String> digital) {
        candidate[row][col] = digital;
    }

    public void makeMoveWithoutCandidate(int row, int col, String value, boolean isMutable) {
        if (this.isValidValue(value) && this.isValidMove(row, col, value) && this.isSlotMutable(row, col)) {
            this.board[row][col] = value;
            this.mutable[row][col] = isMutable;
        }
    }

    public void makeMutable(int row, int col, boolean isMutable) {
        this.mutable[row][col] = isMutable;
    }

    public boolean isValidMove(int row, int col, String value) {
        makeSureInRange(row, col);
        return !this.numInCol(col, value) && !this.numInRow(row, value) && !this.numInBox(row, col, value);

    }

    public boolean numInCol(int col, String value) {
        if (col <= Const.COLUMNS) {
            for (int row = 0; row < Const.ROWS; row++) {
                if (this.board[row][col].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean numInRow(int row, String value) {
        if (row <= Const.ROWS) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                if (this.board[row][col].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean numInBox(int row, int col, String value) {
        makeSureInRange(row, col);

        int boxRow = row / Const.BOX_HEIGHT;
        int boxCol = col / Const.BOX_WIDTH;

        int startingRow = (boxRow * Const.BOX_HEIGHT);
        int startingCol = (boxCol * Const.BOX_WIDTH);

        for (int r = startingRow; r <= (startingRow + Const.BOX_HEIGHT) - 1; r++) {
            for (int c = startingCol; c <= (startingCol + Const.BOX_WIDTH) - 1; c++) {
                if (this.board[r][c].equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isSlotAvailable(int row, int col) {
        makeSureInRange(row, col);
        return "".equals(this.board[row][col]) && this.isSlotMutable(row, col);
    }

    public boolean isSlotMutable(int row, int col) {
        return this.mutable[row][col];
    }

    public String getValue(int row, int col) {
        makeSureInRange(row, col);
        return this.board[row][col];
    }

    public List<String> getCandidate(int row, int col) {
        makeSureInRange(row, col);
        return candidate[row][col];
    }

    public boolean isSlotValid(int row, int col) {
        return !"".equals(getValue(row, col));
    }

    private boolean isValidValue(String value) {
        for (String str : Const.VALID_VALUES) {
            if (str.equals(value)) { return true; }
        }
        return false;
    }

    public void makeSureInRange(int row, int col) {
        if (row >= Const.ROWS || row < 0 || col >= Const.COLUMNS || col < 0) {
            throw new RuntimeException("row " + row + "\t col " + col + " is not in range");
        }
    }

    public boolean boardFull() {
        for (int r = 0; r < Const.ROWS; r++) {
            for (int c = 0; c < Const.COLUMNS; c++) {
                if ("".equals(this.board[r][c])) { return false; }
            }
        }
        return true;
    }

    public void makeSlotEmpty(int row, int col) {
        this.board[row][col] = "";
        //初始化候选值，丢弃所有手动对候选值的修改
        resetCandidate();
    }

    public void makeSlotEmptyWithoutRestCandidate(int row, int col) {
        this.board[row][col] = "";
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Game Board:\n");
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                str.append(this.board[row][col]).append(" ");
            }
            str.append("\n");
        }
        return str.append("\n").toString();
    }

    private void initializeBoard() {
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                this.board[row][col] = "";
            }
        }
    }

    private void initializeMutableSlots() {
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                this.mutable[row][col] = true;
            }
        }
    }

    public void setBoard(String[][] board) {
        this.board = board;
        resetCandidate();
    }

    public Optional<Position> numInRowPosition(int row, String value) {
        if (row < Const.ROWS) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                if (this.board[row][col].equals(value)) {
                    return Optional.of(new Position(row, col));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Position> numInColumnPosition(int column, String value) {
        if (column < Const.COLUMNS) {
            for (int row = 0; row < Const.ROWS; row++) {
                if (this.board[row][column].equals(value)) {
                    return Optional.of(new Position(row, column));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Position> numInBoxPosition(int row, int col, String value) {
        makeSureInRange(row, col);

        int boxRow = row / Const.BOX_HEIGHT;
        int boxCol = col / Const.BOX_WIDTH;

        int startingRow = (boxRow * Const.BOX_HEIGHT);
        int startingCol = (boxCol * Const.BOX_WIDTH);

        for (int r = startingRow; r <= (startingRow + Const.BOX_HEIGHT) - 1; r++) {
            for (int c = startingCol; c <= (startingCol + Const.BOX_WIDTH) - 1; c++) {
                if (this.board[r][c].equals(value)) {
                    return Optional.of(new Position(r, c));
                }
            }
        }

        return Optional.empty();
    }

    public void resetCandidate() {
        this.candidate = initializeCandidate();
    }

    private List[][] initializeCandidate() {
        List[][] result = new ArrayList[Const.ROWS][Const.COLUMNS];
        for (int row = 0; row < Const.ROWS; row++) {
            for (int column = 0; column < Const.COLUMNS; column++) {
                if (isSlotValid(row, column)) {
                    continue;
                }

                Set<String> contain = new HashSet<>(9);
                //获取这行的所有数字
                for (int k = 0; k < Const.ROWS; k++) {
                    if (isSlotValid(row, k)) {
                        contain.add(getValue(row, k));
                    }
                }
                //获取这列的所有数字
                for (int i = 0; i < Const.COLUMNS; i++) {
                    if (isSlotValid(i, column)) {
                        contain.add(getValue(i, column));
                    }
                }
                //获取这宫的所有数字
                int rowStart = row - row % Const.BOX_WIDTH;
                int columnStart = column - column % Const.BOX_WIDTH;
                for (int i = rowStart; i < rowStart + Const.BOX_WIDTH; i++) {
                    for (int j = columnStart; j < columnStart + Const.BOX_WIDTH; j++) {
                        if (isSlotValid(i, j)) {
                            contain.add(getValue(i, j));
                        }
                    }
                }

                Set<String> copy = new HashSet<>(Const.SET_VALUES);
                copy.removeAll(contain);
                List<String> remaining = new ArrayList<>(copy);
                Collections.sort(remaining);
                result[row][column] = remaining;
            }
        }
        return result;
    }

    public Map<Position, List<String>> findRemaining() {
        Map<Position, List<String>> possibleValues = new HashMap<>(81);
        // 找出每个空白格的候选数
        for (int row = 0; row < Const.ROWS; row++) {
            for (int column = 0; column < Const.COLUMNS; column++) {
                if (isSlotValid(row, column)) {
                    continue;
                }

                List<String> listString = candidate[row][column];
                if (CollectionUtils.isEmpty(listString)) {
                    continue;
                }

                possibleValues.put(new Position(row, column), listString);
            }
        }

        return possibleValues;
    }

    public int getNumColumns() {
        return Const.COLUMNS;
    }

    public int getBoxWidth() {
        return Const.BOX_WIDTH;
    }

    public int getBoxHeight() {
        return Const.BOX_HEIGHT;
    }
}
