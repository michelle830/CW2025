package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

/**
 * Coordinates user input, board updates, and game loop timing for the Tetris game.
 * <p>
 * Acts as the main controller in the MVC pattern, managing communication
 * between the game logic ({@link Board}) and the user interface ({@link GuiController}).
 * It handles all game events including movement, rotation, hard drop and timer modes,
 * score updates and game-over transitions
 *
 * @author Chan Michelle
 * @version 1.0
 */
public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;
    private IntegerProperty timeRemaining = new SimpleIntegerProperty(-1);

    private boolean gameEnded = false;
    private Timeline countdown;
    private String leaderboardFile;

    /**
     * Creates a normal (non-timed) game controller.
     *
     * @param guiController GUI controller used to render the game state
     */
    public GameController(GuiController guiController) {
        this(guiController, -1);
        leaderboardFile = "normal";
    }

    /**
     * Creates a game controller with an optional time limit.
     *
     * @param guiController GUI controller used to render the game state
     * @param timeLimitSeconds  time limit in seconds, or a negative value for no time limit
     */
    public GameController(GuiController guiController, int timeLimitSeconds) {
        this.viewGuiController = guiController;
        this.board = new SimpleBoard(10, 25);
        this.timeRemaining.set(timeLimitSeconds);

        boolean gameOverOnStart = board.createNewBrick();

        guiController.setGameController(this);
        guiController.setEventListener(this);
        guiController.initGameView(board.getBoardMatrix(), board.getViewData());

        if (timeLimitSeconds > 0) {
            leaderboardFile = "time_" + timeLimitSeconds;
            guiController.bindTime(timeRemaining);
            guiController.enableTimerDisplay();
            startCountdown();
        }

        if (gameOverOnStart) {
            handleGameOver();
        }
    }

    /**
     * @return the current score form the underlying board
     */
    public int getScore() {
        return board.getScore().scoreProperty().get();
    }

    private void startCountdown() {
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (gameEnded) return;

            int timeRemainingSeconds = timeRemaining.get();

            if (timeRemainingSeconds <= 0) {
                timeRemaining.set(0);
                gameEnded = true;
                viewGuiController.gameOver();
                return;
            }

            timeRemaining.set(timeRemainingSeconds - 1);
        }));

        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    /**
     * Adds score and updates GUI if any rows were cleared.
     *
     * @param clearRow result of the row-clear operation
     */
    private void applyCLearRow (ClearRow clearRow) {
        if (clearRow != null && clearRow.linesRemoved() > 0) {
            board.getScore().add(clearRow.scoreBonus());
            viewGuiController.updateScore(board.getScore().scoreProperty().get());
        }
    }

    /**
     * Handles transition to game-over state (only once).
     */
    private void handleGameOver() {
        if (gameEnded) {
            return;
        }
        gameEnded = true;
        viewGuiController.gameOver();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean moved = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!moved) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            applyCLearRow(clearRow);

            boolean gameOver = board.createNewBrick();
            if (gameOver) {
                handleGameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public void onHoldEvent() {
        board.holdCurrentBrick();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        gameEnded = false;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public ViewData onHardDropEvent() {
        while (board.moveBrickDown()) {
            // keep dropping until collision
        }

        board.mergeBrickToBackground();
        ClearRow clear = board.clearRows();
        applyCLearRow(clear);

        boolean gameOver = board.createNewBrick();
        if (gameOver) {
            handleGameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }

    /**
     * @return the leaderboard file prefix used for the current game mode
     */
    public String getLeaderboardFile() {
        return leaderboardFile;
    }
}
