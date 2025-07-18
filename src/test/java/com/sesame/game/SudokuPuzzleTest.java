package com.sesame.game;

import java.util.List;

import com.sesame.game.common.SudokuPuzzle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SudokuPuzzleTest {

    private SudokuPuzzle puzzle;

    @Before
    public void setUp() {
        /*
         * Here is the puzzle
         * 0 0 8 3 4 2 9 0 0
         * 0 0 9 0 0 0 7 0 0
         * 4 0 0 0 0 0 0 0 3
         * 0 0 6 4 7 3 2 0 0
         * 0 3 0 0 0 0 0 1 0
         * 0 0 2 8 5 1 6 0 0
         * 7 0 0 0 0 0 0 0 8
         * 0 0 4 0 0 0 1 0 0
         * 0 0 3 6 9 7 5 0 0
         */
        String[][] board = new String[][] {
            {"0", "0", "8", "3", "4", "2", "9", "0", "0"},
            {"0", "0", "9", "0", "0", "0", "7", "0", "0"},
            {"4", "0", "0", "0", "0", "0", "0", "0", "3"},
            {"0", "0", "6", "4", "7", "3", "2", "0", "0"},
            {"0", "3", "0", "0", "0", "0", "0", "1", "0"},
            {"0", "0", "2", "8", "5", "1", "6", "0", "0"},
            {"7", "0", "0", "0", "0", "0", "0", "0", "8"},
            {"0", "0", "4", "0", "0", "0", "1", "0", "0"},
            {"0", "0", "3", "6", "9", "7", "5", "0", "0"}
        };
        puzzle = new SudokuPuzzleForTesting(board);
    }

    @Test
    public void testNumInRow() {
        Assert.assertTrue(puzzle.numInRow(0, "9"));
        Assert.assertTrue(puzzle.numInRow(1, "7"));
        Assert.assertFalse(puzzle.numInRow(8, "1"));
    }

    @Test
    public void testNumInCol() {
        Assert.assertTrue(puzzle.numInCol(0, "4"));
        Assert.assertTrue(puzzle.numInCol(5, "2"));
        Assert.assertFalse(puzzle.numInCol(8, "1"));
    }

    @Test
    public void testNumInBox() {
        Assert.assertTrue(puzzle.numInBox(6, 1, "4"));
        Assert.assertFalse(puzzle.numInBox(4, 4, "2"));
        Assert.assertTrue(puzzle.numInBox(4, 4, "8"));
    }

    @Test
    public void testMakeMove() {
        String[][] board = new String[][] {
            {"3", "", "7", "1", "", "6", "", "", "9"},
            {"", "", "", "2", "", "9", "3", "6", ""},
            {"", "6", "9", "3", "", "8", "", "", ""},
            {"", "1", "2", "", "6", "3", "", "", "5"},
            {"", "3", "", "5", "9", "1", "", "", ""},
            {"", "5", "", "", "8", "2", "", "", ""},
            {"6", "", "1", "8", "", "4", "9", "5", ""},
            {"4", "", "", "9", "1", "5", "", "", ""},
            {"", "9", "", "6", "", "7", "4", "1", "8"}
        };

        puzzle = new SudokuPuzzleForTesting(board);
        puzzle.resetCandidate();

        List<String> candidate = puzzle.getCandidate(5, 2);
        Assert.assertEquals(2, candidate.size());
        Assert.assertEquals("4", candidate.get(0));
        Assert.assertEquals("6", candidate.get(1));

        candidate = puzzle.getCandidate(5, 3);
        Assert.assertEquals(2, candidate.size());
        Assert.assertEquals("4", candidate.get(0));
        Assert.assertEquals("7", candidate.get(1));

        candidate = puzzle.getCandidate(5, 8);
        Assert.assertEquals(5, candidate.size());

        candidate = puzzle.getCandidate(0, 7);
        Assert.assertEquals(3, candidate.size());
        Assert.assertEquals("2", candidate.get(0));
        Assert.assertEquals("4", candidate.get(1));
        Assert.assertEquals("8", candidate.get(2));

        candidate = puzzle.getCandidate(3, 7);
        Assert.assertEquals(4, candidate.size());
        Assert.assertEquals("4", candidate.get(0));
        Assert.assertEquals("7", candidate.get(1));
        Assert.assertEquals("8", candidate.get(2));
        Assert.assertEquals("9", candidate.get(3));

        puzzle.makeMove(5, 7, "4", true);

        candidate = puzzle.getCandidate(5, 2);
        Assert.assertEquals(1, candidate.size());
        Assert.assertEquals("6", candidate.get(0));

        candidate = puzzle.getCandidate(5, 3);
        Assert.assertEquals(1, candidate.size());
        Assert.assertEquals("7", candidate.get(0));

        candidate = puzzle.getCandidate(5, 8);
        Assert.assertEquals(4, candidate.size());

        candidate = puzzle.getCandidate(0, 7);
        Assert.assertEquals(2, candidate.size());
        Assert.assertEquals("2", candidate.get(0));
        Assert.assertEquals("8", candidate.get(1));

        candidate = puzzle.getCandidate(3, 7);
        Assert.assertEquals(3, candidate.size());
        Assert.assertEquals("7", candidate.get(0));
        Assert.assertEquals("8", candidate.get(1));
        Assert.assertEquals("9", candidate.get(2));

    }

    @Test
    public void testMakeSlotEmpty_ShouldClearValueAndRestoreCandidates() {
        // 1. Setup
        String[][] board = new String[][] {
            {"", "2", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "1", "", "", "8", ""}, // Peer in same box
            {"1", "", "", "", "5", "", "", "", "9"}, // Target row, with peers
            {"", "", "", "", "2", "", "", "7", ""}, // Peer in same box
            {"", "", "", "", "", "", "", "", ""},
            {"", "4", "", "", "", "", "", "", ""}, // Peer in same column
            {"", "", "", "", "", "", "", "", ""}
        };
        SudokuPuzzle puzzle = new SudokuPuzzleForTesting(board);
        puzzle.resetCandidate();

        // Pre-assertion: Check initial state
        Assert.assertEquals("5", puzzle.getValue(4, 4));
        Assert.assertFalse("Candidate '5' should not exist in peer cell before clearing",
            puzzle.getCandidate(4, 1).contains("5")); // Peer in same row
        Assert.assertFalse("Candidate '5' should not exist in peer cell before clearing",
            puzzle.getCandidate(7, 4).contains("5")); // Peer in same column
        Assert.assertFalse("Candidate '5' should not exist in peer cell before clearing",
            puzzle.getCandidate(3, 3).contains("5")); // Peer in same box

        // 2. Action
        puzzle.makeSlotEmpty(4, 4);

        // 3. Assertions
        // Assert the target cell is now empty
        Assert.assertEquals("The cell should be empty", "", puzzle.getValue(4, 4));

        // Assert the target cell's candidates are restored
        List<String> targetCandidates = puzzle.getCandidate(4, 4);
        Assert.assertTrue("Candidates for the cleared cell should be recalculated and contain '5'",
            targetCandidates.contains("5"));
        Assert.assertTrue("Candidates should contain '3'", targetCandidates.contains("3"));
        Assert.assertTrue("Candidates should contain '6'", targetCandidates.contains("6"));

        // Assert peers' candidates are updated
        Assert.assertTrue("Peer in the same row should now have '5' as a candidate",
            puzzle.getCandidate(4, 1).contains("5"));
        Assert.assertTrue("Peer in the same column should now have '5' as a candidate",
            puzzle.getCandidate(7, 4).contains("5"));
        Assert.assertTrue("Peer in the same box should now have '5' as a candidate",
            puzzle.getCandidate(3, 3).contains("5"));

        // Assert an unrelated cell's candidates are unchanged
        List<String> unrelatedCandidatesBefore = puzzle.getCandidate(0, 0);
        // Re-call makeSlotEmpty to ensure it doesn't change anything
        puzzle.makeSlotEmpty(4, 4);
        List<String> unrelatedCandidatesAfter = puzzle.getCandidate(0, 0);
        Assert.assertEquals("Candidates of unrelated cells should not be affected",
            unrelatedCandidatesBefore, unrelatedCandidatesAfter);

    }

    private class SudokuPuzzleForTesting extends SudokuPuzzle {
        public SudokuPuzzleForTesting(String[][] board) {
            super();
            this.board = board;
        }
    }
}
