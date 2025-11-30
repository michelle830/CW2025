/**
 * Immutable data object that describes what the GUI needs to draw:
 * -The current falling brick (its matrix and position on the board)
 * -The "ghost" landing position of that brick
 * -The next brick to be displayed in the preview panel
 * Refactored for COMP2042 to:
 * -Improve clarity of purpose
 * -Add Javadoc
 * -Use clearer naming conventions
 * -Maintain immutability with defensive copying
 */

package com.comp2042;

public final class ViewData {

    private final int[][] brickMatrix;
    private final int x;
    private final int y;

    private final int[][] nextBrickMatrix;

    private final int[][] ghostMatrix;
    private final int ghostX;
    private final int ghostY;

    private final int[][] holdShape;

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

    //** Current falling brick matrix */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickMatrix);
    }

    /** Falling brick X position */
    public int getxPosition() { return x; }

    /** Falling brick Y position */
    public int getyPosition() { return y; }

    /** Preview (next) brick matrix */
    public int[][] getNextBrickData() {
        return (nextBrickMatrix != null) ? MatrixOperations.copy(nextBrickMatrix) : null;
    }

    /** Ghost piece matrix */
    public int[][] getGhostData() {
        return (ghostMatrix != null) ? MatrixOperations.copy(ghostMatrix) : null;
    }

    /** Ghost piece X position */
    public int getghostX() { return ghostX; }

    /** Ghost piece Y position */
    public int getghostY() { return ghostY; }

    public int[][] getHoldShape() {
        return (holdShape != null) ? MatrixOperations.copy(holdShape) : null;
    }

    public int[][] getHoldData() {
        return getHoldShape();
    }
}