package com.comp2042;

import javafx.fxml.FXML;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixOperationsTest {

    @Test
    void testCopyMatrix() {
        int[][] original = {{1, 2}, {3, 4}};
        int[][] copy = MatrixOperations.copy(original);

        assertArrayEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void testMergeMatrix() {
        int[][] matrix = new int[4][4];
        int[][] brick = {{1, 0}, {1, 0}};

        int[][] merged = MatrixOperations.merge(matrix, brick, 0, 0);

        assertEquals(1, merged[0][0]);
        assertEquals(1, merged[1][0]);
    }

    @Test
    void testRowClear() {
        int[][] board = new int[4][4];

        // Fill bottom row
        for (int i = 0; i < 4; i++) {
            board[3][i] = 1;
        }

        ClearRow cr = MatrixOperations.checkRemoving(board);

        assertEquals(1, cr.linesRemoved());
        assertEquals(50, cr.scoreBonus());
    }
}
