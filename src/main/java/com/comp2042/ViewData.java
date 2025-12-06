package com.comp2042;

/**
 * Immutable data transfer object that provides all necessary information
 * for the GUI to render a single frame of the Tetris game.
 *
 * <p>This snapshot includes:</p>
 * <ul>
 *     <li>The current falling brick (matrix + position)</li>
 *     <li>The ghost landing position of that brick</li>
 *     <li>The next brick preview</li>
 *     <li>The held brick (if any)</li>
 * </ul>
 *
 *
 * <p>
 * Refactored for COMP2042 to:
 * <ul>
 *     <li>Improve documentation clarity</li>
 *     <li>Ensure immutability using defensive copying</li>
 *     <li>Follow clean-code naming conventions</li>
 * </ul>
 *
 * @author Chan Michelle
 * @version 1.0
 */

public final class ViewData {

    /** Current falling brick shape matrix. */
    private final int[][] brickMatrix;

    /** X position of the falling brick on the board. */
    private final int x;

    /** Y position of the falling brick on the board. */
    private final int y;

    /** Matrix for the next brick preview. */
    private final int[][] nextBrickMatrix;

    /** Matrix representing ghost piece shape. */
    private final int[][] ghostMatrix;

    /** X coordinate of the ghost piece. */
    private final int ghostX;

    /** Y coordinate of the ghost piece. */
    private final int ghostY;

    /** Matrix for the hold brick (first rotation). */
    private final int[][] holdShape;

    /**
     * Constructs an immutable snapshot of the current game view data.
     *
     * @param brickMatrix       the current falling brick matrix
     * @param x                 X position of the falling brick
     * @param y                 Y position of the falling brick
     * @param nextBrickMatrix   the next brick preview
     * @param ghostMatrix       the ghost piece representation
     * @param ghostX            X position of the ghost piece
     * @param ghostY            Y position of the ghost piece
     * @param holdShape         the hold brick matrix
     */
    public ViewData(int[][] brickMatrix, int x, int y, int[][]nextBrickMatrix, int[][] ghostMatrix, int ghostX, int ghostY, int[][] holdShape) {

        this.brickMatrix = MatrixOperations.copy(brickMatrix);
        this.x = x;
        this.y = y;

        this.nextBrickMatrix = (nextBrickMatrix != null) ? MatrixOperations.copy(nextBrickMatrix) :  null;
        this.ghostMatrix = (ghostMatrix != null) ? MatrixOperations.copy(ghostMatrix) : null;
        this.ghostX = ghostX;
        this.ghostY = ghostY;
        this.holdShape = (holdShape != null) ? MatrixOperations.copy(holdShape) : null;
    }

    //--------------------------------------------------------------------
    // Accessors (all return defensive copies to preserve immutability)
    //--------------------------------------------------------------------

    /** @return a copy of the falling brick matrix */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickMatrix);
    }

    /** @return the X position of the falling brick */
    public int getxPosition() { return x; }

    /** @return the y position of the falling brick */
    public int getyPosition() { return y; }

    /** @return matrix of the next brick preview, or null */
    public int[][] getNextBrickData() {
        return (nextBrickMatrix != null) ? MatrixOperations.copy(nextBrickMatrix) : null;
    }

    /** @return matrix of the ghost piece, or null */
    public int[][] getGhostData() {
        return (ghostMatrix != null) ? MatrixOperations.copy(ghostMatrix) : null;
    }

    /** @return X coordinate of the ghost piece */
    public int getghostX() { return ghostX; }

    /** @return Y coordinate of the ghost piece */
    public int getghostY() { return ghostY; }

    /** @return a copy of the held brick matrix, or null */
    public int[][] getHoldShape() {
        return (holdShape != null) ? MatrixOperations.copy(holdShape) : null;
    }

    /**
     * Backward-compatibility alias for older GUI code.
     * Equivalent to {@link #getHoldShape()}.
     */
    public int[][] getHoldData() {
        return getHoldShape();
    }
}