package com.comp2042;

/**
 * Stores rotation preview data for a Tetromino.
 *
 * <p>This object contains:</p>
 * <ul>
 *     <li>The rotated shape matrix</li>
 *     <li>The rotation index</li>
 * </ul>
 *
 * <p>Used by BrickRotator and SimpleBoard to safely apply rotations.</p>
 */

public final class NextShapeInfo {

    private final int[][] shapeMatrix;
    private final int rotationIndex;

    /**
     * Constructs a NextShapeInfo object
     *
     * @param shapeMatrix Rotated shape matrix
     * @param rotationIndex Rotation index
     */
    public NextShapeInfo(final int[][] shapeMatrix, final int rotationIndex) {
        this.shapeMatrix = shapeMatrix;
        this.rotationIndex = rotationIndex;
    }

    /**
     * @return Defensive copy of rotated brick shape
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shapeMatrix);
    }

    /**
     * @return Rotation index
     */
    public int getPosition() {
        return rotationIndex;
    }
}
