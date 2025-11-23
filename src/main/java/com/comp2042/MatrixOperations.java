package com.comp2042;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class that provides core matrix operations for the Tetris game.
 *
 * Includes:
 * - Collision detection (intersect)
 * - Board copying
 * - Brick merging into the board
 * - Row clearing logic
 * - Deep copying of brick rotation matrices
 *
 * Refactored for COMP2042 to improve documentation and readability.
 */
public final class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){
    }

    /**
     * Determine if a brick(shape matrix) will collide with existing board cells.
     *
     * @param matrix board matrix
     * @param brick shape matrix of the brick
     * @param x top left X position on the board
     * @param y top left Y position on the board
     * @return true if collision occurs, false if safe
     */

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *Checks if a matrix coordinate is out of bounds
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        return !(targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length );
    }

    /**
     * Returns a deep copy of a 2D matrix.
     */
    public static int[][] copy(int[][] original) {
        int[][] copied = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] row = original[i];
            System.arraycopy(row, 0, copied[i], 0, row.length);
        }
        return copied;
    }

    /**
     * Merges a brick into the board matrix at the given position
     *
     * @param filledFields existing board matrix
     * @param brick shape matrix of brick
     * @param x X coordinate
     * @param y Y coordinate
     * @return new board matrix with brick merged
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
     * Checks the board  for full rows, removes them,shifts remaining rows down,
     * and calculates score bonys
     *
     * @param matrix board matrix
     * @return ClearRow object containing number of cleared rows, new matrix, and score bonus
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix[0].length];
        Deque<int[]> remainingRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] row = matrix[i];
            boolean fullRow = true;

            for (int value : row) {
                if (value == 0) {
                    fullRow = false;
                    break;
                }
            }

            if (fullRow) {
                clearedRows.add(i);
            } else {
                remainingRows.add(rows.clone());
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = remainingRows.pollLast();
            if (row != null) {
                newMatrix[i] = row;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), newMatrix, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
