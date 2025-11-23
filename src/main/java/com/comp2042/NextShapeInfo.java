/**
 * Represents the next rotation state of a Tetromino
 *
 * A NextShapeInfo object contains:
 * -The rotated shape matrix
 * -The rotation index (0-3 depending on the brick)
 *
 * This is used by BrickRotator and SimpleBoard to preview and
 * apply rotation safely.
 *
 * Refactored for COMP2042 to improve clarity and documentation
 */
package com.comp2042;

public final class NextShapeInfo {

    private final int[][] shapeMatrix;
    private final int rotationIndex;

    /**
     * Constructs a NextShapeInfo object
     *
     * @param shapeMatrix matrix representing the rotated brick
     * @param rotationIndex index of this rotation state
     */
    public NextShapeInfo(final int[][] shapeMatrix, final int rotationIndex) {
        this.shapeMatrix = shapeMatrix;
        this.rotationIndex = rotationIndex;
    }

    /**
     * @return a defensive copy of the rotation matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shapeMatrix);
    }

    /**
     * @return the rotation index associated with this shape
     */
    public int getPosition() {
        return rotationIndex;
    }
}
