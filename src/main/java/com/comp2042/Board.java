package com.comp2042;

/**
 * Core abstraction for the Tetris board logic.
 * <p>
 * Implementations handle movement, rotation, line clearing, scoring,
 * and hold/next brick features independently of the GUI
 */

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    /**
     * Creates a new active brick at the spawn position
     *
     * @return {@code true} if the brick cannot be placed (game over),{@code false} otherwise
     */

    boolean createNewBrick();

    /**
     * @return current board matrix including all fixed bricks
     */
    int[][] getBoardMatrix();

    /**
     * @return snapshot of the current view-related state (brick, ghost, hold, next)
     */
    ViewData getViewData();

    /**
     * Merges the current falling brick into the static board matrix.
     */
    void mergeBrickToBackground();

    /**
     * Clears any full rows and returns information about the cleared rows.
     *
     * @return {@link ClearRow} describing the clear operation
     */
    ClearRow clearRows();

    /**
     * @return score model associated with this board
     */
    Score getScore();

    /**
     * Resets the board to a new empty game state.
     */
    void newGame();

    /**
     * New feature: allow holding/swapping the current brick
     * */
    void holdCurrentBrick();
}
