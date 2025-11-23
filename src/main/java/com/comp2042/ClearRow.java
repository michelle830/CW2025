/**
 * Represents the result of clearing rows after a brick merge.
 * Stores both:
 * - how many rows were removed
 * - the score bonus earned for those removals
 * Refactored for COMP2042 to improve naming, clarity and documentation
 */

package com.comp2042;

public record ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {

    /**
     * Constructs a ClearRow object
     *
     * @param linesRemoved number of rows removed
     * @param newMatrix    updated board state after rows were cleared
     * @param scoreBonus   score awarded for the removal
     */
    public ClearRow {
    }

    /**
     * how many times were removed
     */
    @Override
    public int linesRemoved() {
        return linesRemoved;
    }

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
