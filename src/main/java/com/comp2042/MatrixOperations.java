package com.comp2042;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing all core matrix operations used in the Tetris game.
 *
 * <p>This class supports:</p>
 * <ul>
 *     <li>Collision detection</li>
 *     <li>Board copying</li>
 *     <li>Brick merging</li>
 *     <li>Row clearing</li>
 *     <li>Deep copying of brick rotation matrices</li>
 * </ul>
 *
 * <p>This class is stateless and cannot be instantiated.</p>
 */
public final class MatrixOperations {

    /** Prevent instantiation of this utility class */
    private MatrixOperations(){
    }

    /**
     * Checks whether a brick collides with the board at a given position.
     *
     * @param matrix Board matrix
     * @param brick  Brick shape matrix
     * @param x      X position on board
     * @param y      Y position on board
     * @return true if collision occurs, false otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;

                if (brick[j][i] != 0 && (isOutOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *Checks whether a given coordinate is outside the board
     */
    private static boolean isOutOfBounds(int[][] matrix, int targetX, int targetY) {
        return !(targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length);
    }

    /**
     * Creates a deep copy of a 2D matrix.
     *
     * @param original Original matrix
     * @return Deep copied matrix
     */
    public static int[][] copy(int[][] original) {
        int[][] copied = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] row = original[i];
            copied[i] = new int[row.length];
            System.arraycopy(row, 0, copied[i], 0, row.length);
        }
        return copied;
    }

    /**
     * Merges a brick into the board matrix at the given position
     *
     * @param filledFields Existing board matrix
     * @param brick        Brick shape matrix
     * @param x            X position
     * @param y            Y position
     * @return New merged board matrix
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);

        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Removes completed rows from the board and calculates score bonus.
     *
     * @param matrix Board matrix
     * @return ClearRow result containing removed lines and new board state
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix[0].length];
        Deque<int[]> remainingRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] row = matrix[i];

            if (isRowFull(row)) {
                clearedRows.add(i);
            } else {
                remainingRows.add(row.clone());
            }
        }

        // Fill from bottom with remaining rows
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = remainingRows.pollLast();
            if (row != null) {
                newMatrix[i] = row;
            }
        }

        int linesCleared = clearedRows.size();
        int scoreBonus = 50 * linesCleared * linesCleared;
        return new ClearRow(linesCleared, newMatrix, scoreBonus);
    }

    /**
     * Determines if a row is completely filled.
     */
    private static boolean isRowFull(int[] row) {
        for (int value : row) {
            if (value == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a deep copy of a list of matrices
     *
     * @param list List of matrices
     * @return Deep copied list
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}
