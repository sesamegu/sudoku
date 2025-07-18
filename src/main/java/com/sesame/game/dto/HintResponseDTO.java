package com.sesame.game.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HintResponseDTO {
    private boolean hintFound;
    private String strategyName;
    private String hintText;
    private List<HighlightUnit> highlightUnits;
    private List<CellPosition> highlightCells;
    private List<CellFill> fillCells;
    private List<CandidateDelete> deleteCandidates;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HighlightUnit {
        private String type; // "ROW", "COLUMN", "BOX"
        private int rowIndex;
        private int colIndex; // for BOX, this is the top-left corner col
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CellPosition {
        private int row;
        private int col;
        private String color; // e.g., "blue", "green" for different highlight types
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CellFill {
        private int row;
        private int col;
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidateDelete {
        private int row;
        private int col;
        private List<String> values;
    }
} 