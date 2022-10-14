package com.sesame.game;

public class SudokuPuzzle {

    protected String[][] board;
    // Table to determine if a slot is mutable
    protected boolean[][] mutable;

    public SudokuPuzzle() {
        this.board = new String[Const.ROWS][Const.COLUMNS];
        this.mutable = new boolean[Const.ROWS][Const.COLUMNS];
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
    }

    public int getNumRows() {
        return Const.ROWS;
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

    public void makeMove(int row, int col, String value, boolean isMutable) {
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
        return this.board[row][col].equals("") && this.isSlotMutable(row, col);
    }

    public boolean isSlotMutable(int row, int col) {
        return this.mutable[row][col];
    }

    public String getValue(int row, int col) {
        makeSureInRange(row, col);
        return this.board[row][col];
    }

    public boolean isSlotValid(int row, int col) {
        return "".equals(getValue(row, col)) ? false : true;
    }

    private boolean isValidValue(String value) {
        for (String str : Const.VALID_VALUES) {
            if (str.equals(value)) { return true; }
        }
        return false;
    }

    public void makeSureInRange(int row, int col) {
        if (row < Const.ROWS && col < Const.COLUMNS && row >= 0 && col >= 0) {
        } else {
            throw new RuntimeException("row " + row + "\t col " + col + " is not in range");
        }
    }

    public boolean boardFull() {
        for (int r = 0; r < Const.ROWS; r++) {
            for (int c = 0; c < Const.COLUMNS; c++) {
                if (this.board[r][c].equals("")) { return false; }
            }
        }
        return true;
    }

    public void makeSlotEmpty(int row, int col) {
        this.board[row][col] = "";
    }

    @Override
    public String toString() {
        String str = "Game Board:\n";
        for (int row = 0; row < Const.ROWS; row++) {
            for (int col = 0; col < Const.COLUMNS; col++) {
                str += this.board[row][col] + " ";
            }
            str += "\n";
        }
        return str + "\n";
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
    }

}
