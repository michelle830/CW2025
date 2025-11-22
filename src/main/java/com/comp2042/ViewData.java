/**
 * Immutable data object that describes what the GUI needs to draw:
 * -The current falling brick (its matrix and position on the board)
 * -The next brick to be displayed in the preview panel
 *
 * Refactored for COMP2042 to:
 * -Improve clarity of purpose
 * -Add Javadoc
 * -Use clearer naming conventions
 * -Maintain immutability with defensive copying
 */

package com.comp2042;

public final class ViewData {

    /** 2D matrix representing the current falling brick */
    private final int[][] currentBrickMatrix;

    private final int x;
    private final int y;
    private final int[][] nextBrickMatrix;

    /**
     * Constructs a ViewData object containing the drawable state of the game.
     *
     * @param currentBrickMatrix  shape matrix of the current falling brick
     * @param x  horizontal board coordinate of current brick
     * @param y  vertical board coordinate of current brick
     * @param nextBrickMatrix  shape matrix of the next brick for the preview
     */
    public ViewData(int[][] currentBrickMatrix, int x, int y, int[][] nextBrickMatrix) {
        this.currentBrickMatrix = currentBrickMatrix;
        this.x = x;
        this.y = y;
        this.nextBrickMatrix = nextBrickMatrix;
    }

    /**
     * @return a defensive copy of the current brick's shape matrix
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(currentBrickMatrix);
    }

    /**
     * @return X position of the current brick on the board
     */
    public int getxPosition() {
        return x;
    }

    /**
     * @return Y positon of the current brick on the board
     */
    public int getyPosition() {
        return y;
    }

    /**
     * @return a defensive copy of the next brick's shape matrix
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickMatrix);
    }
}
