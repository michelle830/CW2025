/**
 * Represents the result of clearing rows after a brick mergre.
 * Stores both:
 * - how many rows were removed
 * - the score bonus earned for those removals
 *
 * Refactored for COMP2042 to improve naming, clarity and documentation
 */

package com.comp2042;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a ClearRow object
     *
     * @param linesRemoved number of rows removed
     * @param newMatrix updated board state after rows were cleared
     * @param scoreBonus score awarded for the removal
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**how many times were removed*/
    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /** score bonue earned for the cleared rows*/
    public int getScoreBonus() {
        return scoreBonus;
    }
}
