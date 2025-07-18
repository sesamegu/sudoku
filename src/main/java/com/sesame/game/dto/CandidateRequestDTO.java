package com.sesame.game.dto;

import lombok.Data;

@Data
public class CandidateRequestDTO {
    private int row;
    private int col;
    private String value;
    private SudokuPuzzleDTO puzzle;
} 