package com.sesame.game.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellDTO {
    private int row;
    private int col;
    private String value;
    private boolean mutable;
    private List<String> candidates;
} 