// File



package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;
    private IntegerProperty timeRemaining = new SimpleIntegerProperty(-1);
    private boolean gameEnded = false;
    private Timeline countdown;
    private String leaderboardFile;

    public GameController(GuiController guiController) {
        this(guiController, -1);
        leaderboardFile = "normal";
    }

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
            viewGuiController.gameOver();
        }
    }

    public int getScore() {
        return board.getScore().scoreProperty().get();
    }

    private void startCountdown() {
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (gameEnded) return;

            int timeRemainingSeconds = timeRemaining.get();

            if (timeRemainingSeconds <= 1) {
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

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean moved = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!moved) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            if (clearRow.linesRemoved() > 0) {
                board.getScore().add(clearRow.scoreBonus());
                viewGuiController.updateScore(board.getScore().scoreProperty().get());
            }

            boolean gameOver = board.createNewBrick();
            if (gameOver && !gameEnded) {
                gameEnded = true;
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
                viewGuiController.updateScore(board.getScore().scoreProperty().get());
            }
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
        }

        board.mergeBrickToBackground();
        ClearRow clear = board.clearRows();

        if (clear.linesRemoved() > 0) {
            board.getScore().add(clear.scoreBonus());
            viewGuiController.updateScore(board.getScore().scoreProperty().get());
        }

        boolean gameOver = board.createNewBrick();
        if (gameOver && !gameEnded) {
            gameEnded = true;
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return board.getViewData();
    }

    public String getLeaderboardFile() {
        return leaderboardFile;
    }
}
