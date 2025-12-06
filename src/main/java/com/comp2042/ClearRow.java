package com.comp2042;

/**
 * Represents the result of clearing rows after a brick merge.
 * <p>
 * Stores:
 * <ul>
 *     <li> how many rows were removed</li>
 *     <li> the new board matrix</li>
 *     <li> the score bonus earned</li>
 * </ul>
 * Refactored for COMP2042 to improve naming, clarity and documentation.
 */

public record ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {

    /**
     * Constructs a {@code ClearRow} object.
     *
     * @param linesRemoved number of rows removed
     * @param newMatrix    updated board state after rows were cleared
     * @param scoreBonus   score awarded for the removal
     */
    public ClearRow {
        // compact constructor is fine; MatrixOperations,copy is used in accessor
    }

    /**
     * how many times were removed
     */
    @Override
    public int linesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a defensive copy of the new board matrix
     *
     * @return copied board matrix
     */
    @Override
    public int[][] newMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * score bonus earned for the cleared rows
     */
    @Override
    public int scoreBonus() {
        return scoreBonus;
    }
}
