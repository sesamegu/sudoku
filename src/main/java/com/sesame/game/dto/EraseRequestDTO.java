package com.sesame.game.dto;

import lombok.Data;

@Data
public class EraseRequestDTO {
    private int row;
    private int col;
    private SudokuPuzzleDTO puzzle;
} 