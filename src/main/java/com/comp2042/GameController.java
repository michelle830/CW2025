/**
 * Coordinates user input, board updates and game loop timing.
 * Refactored for COMP2042 to seperate responsibilities, reduce duplication
 * and improve clarity while preserving original gameplay behavior.
 */

package com.comp2042;

public class GameController implements InputEventListener {

    /** Core game logic (board state, movement, collision, score) */
    private final Board board;

    /** UI controller responsible for drawing the board and receiving user input */
    private final GuiController viewGuiController;

    /**
     * Constructor that sets up a new game session.
     *
     * @param guiController The JavaFX controller responsible
     */
    public GameController(GuiController guiController) {
        this.viewGuiController = guiController;
        this.board = new SimpleBoard(10,25);

        boolean gameOverOnStart = board.createNewBrick();

        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());

        // Bind score property directly to GUI label
        viewGuiController.bindScore(board.getScore().scoreProperty());

        if (gameOverOnStart) {
            viewGuiController.gameOver();
        }
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean moved = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!moved) {
            // Merge brick into background
            board.mergeBrickToBackground();

            // Clear rows (if any)
            clearRow = board.clearRows();

            // Add score for line clears
            if (clearRow.linesRemoved() > 0) {
                board.getScore().add(clearRow.scoreBonus());
            }

            // Try spawning next brick -> if fails,game over
            boolean gameOver = board.createNewBrick();
            if (gameOver) {
                viewGuiController.gameOver();
            }

            // Redraw background grid after merge
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else {
            // Reward manual down movement (soft drop)
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Move the current brick left
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }
    /**
     * Move the current brick right
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }
    /**
     * Rotate the active brick
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Resets board & score and redraws the starting background
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
