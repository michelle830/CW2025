package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main JavaFX controller for the Tetris game screen.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Handles keyboard input and forwards events to {@link GuiController} via {@link InputEventListener}.</li>
 *     <li>Renders the board background, active brick, ghost piece, hold brick and next preview.</li>
 *     <li>Manages pause/resume, in-game menu overlay and timer label.</li>
 *     <li>Displays game over-panel and opens the leaderboard window.</li>
 * </ul>
 * <p>
 * This controller is wired to {@code gameLayout.fxml}.
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 30;
    private static final double GHOST_ALPHA = 0.3;

    // FXML-Injected nodes

    @FXML
    private GridPane gamePanel;

    @FXML
    private Pane brickPanel;

    @FXML
    private Pane ghostPanel;

    @FXML
    private GridPane holdPanel;

    @FXML
    private GridPane nextPanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Group pauseOverlay;

    @FXML
    private Button pauseButton;

    @FXML
    private StackPane boardStack;

    @FXML
    private Pane rootPane;

    @FXML
    private Group menuOverlay;

    @FXML
    private Button menuButton;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timerLabel;

    // State Fields
    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Timeline timeLine;
    private GameController gameController;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private ActionEvent event;

    // FXML Event Handlers

    @FXML
    public void pauseButtonClicked(ActionEvent actionEvent) {
        togglePause();
    }

    @FXML
    private void exitButtonClicked() {
        System.exit(0);
    }

    @FXML
    private void restartButtonClicked() {
        newGame(event);
    }

// Initialisation

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL fontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 38);
        }

        StackPane.setAlignment(gamePanel, Pos.TOP_LEFT);
        StackPane.setAlignment(brickPanel, Pos.TOP_LEFT);
        StackPane.setAlignment(ghostPanel, Pos.TOP_LEFT);

        setupKeyboardHandling();

        gameOverPanel.setVisible(false);
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
        }

        if (timerLabel != null) {
            timerLabel.setVisible(false);
        }
    }

    /**
     * Applies the currently selected {@link Theme} to the root game pane
     * and ensures the board remains transparent.
     *
     * @param theme theme to apply
     */
    public void applyTheme(Theme theme) {
        String style;

        if (theme.getType() == Theme.Type.COLOR) {
            style = "-fx-background-color: " + theme.getValue() + ";";
        } else {
            style = "-fx-background-image: url('" + theme.getValue() + "');"
                    + "-fx-background-size: cover;"
                    + "-fx-background-position:center;";
        }

        if (rootPane != null) {
            rootPane.setStyle(style);
        }

        if (boardStack != null) {
            boardStack.setStyle("-fx-background-color: transparent;");
        }

        Platform.runLater(() -> gamePanel.requestFocus());
    }

    /**
     * Sets up keyboard focus and registers the key handler on the game panel.
     */
    private void setupKeyboardHandling() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPressed);
    }

    // Keyboard input handling

    /**
     * Central keyboard handling method; translates key presses into
     * {@link MoveEvent}s and other high-level actions (pause, menu, hold, etc.).
     *
     * @param keyEvent the JavaFX key event
     */
    private void handleKeyPressed(KeyEvent keyEvent) {
        if (!isInputEnabled()) {
            if (keyEvent.getCode() == KeyCode.N) {
                newGame(null);
            }
            if (keyEvent.getCode() == KeyCode.R) {
                togglePause();
            }
            return;
        }

        KeyCode code = keyEvent.getCode();

        switch (code) {
            case LEFT:
            case A:
                refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                keyEvent.consume();
                break;

            case RIGHT:
            case D:
                refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                keyEvent.consume();
                break;

            case UP:
            case W:
                refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                keyEvent.consume();
                break;

            case DOWN:
            case S:
                moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                keyEvent.consume();
                break;

            case N:
                newGame(null);
                keyEvent.consume();
                break;

            case P:
                togglePause();
                keyEvent.consume();
                break;

            case R:
                if (isPause.get()) {
                    togglePause();
                } else {
                    newGame(null);
                }
                keyEvent.consume();
                break;

            case M:
                openMenu();
                keyEvent.consume();
                break;

            case C:
                if (eventListener != null) {
                    eventListener.onHoldEvent();
                    refreshBrick(eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD)).viewData());
                }
                keyEvent.consume();
                break;

            case SPACE:
                if (eventListener != null) {
                    refreshBrick(eventListener.onHardDropEvent());
                }
                keyEvent.consume();
                break;

            default:
                break;
        }
    }

    // Pause / Input State

    private void togglePause() {
        boolean nowPaused = !isPause.get();
        isPause.set(nowPaused);

        if (nowPaused) {
            if (timeLine != null) {
                timeLine.pause();
            }
            if (pauseOverlay != null) {
                pauseOverlay.setVisible(true);
            }
            if (pauseButton != null) {
                pauseButton.setText("Resume");
            }
        } else {
            if (timeLine != null) {
                timeLine.play();
            }
            if (pauseOverlay != null) {
                pauseOverlay.setVisible(false);
            }
            if (pauseButton != null) {
                pauseButton.setText("Pause");
            }
        }

        gamePanel.requestFocus();
    }

    private boolean isInputEnabled() {
        return !isPause.get() && !isGameOver.get();
    }

    // Initial Game View Setup

    /**
     * Initialises the board drawing and starts the automatic game loop timeline.
     *
     * @param boardMatrix initial background board matrix
     * @param brick       initial {@link ViewData} for the falling brick
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        initBackgroundGrid(boardMatrix);
        refreshBrick(brick);
        setupTimeLine();
    }

    private void initBackgroundGrid(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(null);
                rectangle.setStrokeWidth(0);

                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
                gamePanel.setStyle("-fx-padding: 0 0 0 5; -fx-background-insets: 0; -fx-grid-lines-visible: false;");
            }
        }
    }

    private void setupTimeLine() {
        timeLine = new Timeline(new KeyFrame(Duration.millis(400), ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    // Colour helpers

    private Paint getFillColor(int i) {
        switch (i) {
            case 0:
                return Color.TRANSPARENT;
            case 1:
                return Color.AQUA;
            case 2:
                return Color.BLUEVIOLET;
            case 3:
                return Color.DARKGREEN;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.RED;
            case 6:
                return Color.BEIGE;
            case 7:
                return Color.BURLYWOOD;
            default:
                return Color.WHITE;
        }
    }

    private Paint getGhostFill(int value) {
        return value == 0 ? Color.TRANSPARENT : Color.rgb(200, 200, 200, GHOST_ALPHA);
    }

    // Main Render Pipeline

    /**
     * Main UI refresh for the active brick,ghost, hold and next preview.
     * <p>
     * If the game is paused, this method does nothing
     *
     * @param brick {@link ViewData} describing current game state
     */
    private void refreshBrick(ViewData brick) {
        if (isPause.get()) {
            return;
        }

        drawActiveBrick(brick);
        drawGhostBrick(brick);
        refreshHoldPiece(brick.getHoldShape());
        refreshNextPiece(brick.getNextBrickData());

        if (scoreLabel != null && gameController != null) {
            scoreLabel.setText("Score: " + gameController.getScore());
        }
    }

    /**
     * Draws the currently falling brick at its active position.
     *
     * @param brick view data for the active brick
     */
    public void drawActiveBrick(ViewData brick) {
        brickPanel.getChildren().clear();

        int[][] brickData = brick.getBrickData();
        for (int r = 0; r < brickData.length; r++) {
            for (int c = 0; c < brickData[r].length; c++) {
                if (brickData[r][c] == 0) continue;

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(brickData[r][c], rectangle);

                rectangle.setLayoutX(c * BRICK_SIZE);
                rectangle.setLayoutY(r * BRICK_SIZE);

                brickPanel.getChildren().add(rectangle);
            }
        }

        double paddingOffset = 5;
        brickPanel.setTranslateX(brick.getxPosition() * BRICK_SIZE + paddingOffset);
        brickPanel.setTranslateY((brick.getyPosition() - 2) * BRICK_SIZE);
    }

    private void drawGhostBrick(ViewData brick) {
        int[][] ghostData = brick.getGhostData();
        ghostPanel.getChildren().clear();

        if (ghostData == null) {
            return;
        }

        for (int r = 0; r < ghostData.length; r++) {
            for (int c = 0; c < ghostData[r].length; c++) {
                if (ghostData[r][c] == 0) continue;

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getGhostFill(ghostData[r][c]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                rectangle.setStroke(Color.rgb(50, 50, 50, 0.7));
                rectangle.setStrokeWidth(1.2);
                rectangle.setStrokeType(StrokeType.INSIDE);

                rectangle.setLayoutX(c * BRICK_SIZE);
                rectangle.setLayoutY(r * BRICK_SIZE);

                ghostPanel.getChildren().add(rectangle);
            }
        }

        double paddingOffset = 5;
        ghostPanel.setTranslateX(brick.getghostX() * BRICK_SIZE + paddingOffset);
        ghostPanel.setTranslateY((brick.getghostY() - 2) * BRICK_SIZE);
    }

    /**
     * Refreshes the static background board based on the matrix contents.
     *
     * @param board board matrix
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Rectangle rectangle = displayMatrix[i][j];
                int value = board[i][j];

                if (value == 0) {
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(null);
                    rectangle.setStrokeWidth(0);
                } else {
                    rectangle.setFill(getFillColor(value));
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(0.8);
                    rectangle.setStrokeType(StrokeType.INSIDE);
                }
            }
        }
    }

    private void refreshHoldPiece(int[][] holdShape) {
        holdPanel.getChildren().clear();

        if (holdShape == null) return;

        for (int r = 0; r < holdShape.length; r++) {
            for (int c = 0; c < holdShape[r].length; c++) {
                if (holdShape[r][c] == 0) continue;

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(holdShape[r][c]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);

                holdPanel.add(rectangle, c, r);
            }
        }
    }

    private void refreshNextPiece(int[][] nextShape) {
        nextPanel.getChildren().clear();

        if (nextShape == null) return;

        int minCol = Integer.MAX_VALUE, maxCol = Integer.MIN_VALUE;
        int minRow = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE;

        for (int r = 0; r < nextShape.length; r++) {
            for (int c = 0; c < nextShape[r].length; c++) {
                if (nextShape[r][c] != 0) {
                    minCol = Math.min(minCol, c);
                    maxCol = Math.max(maxCol, c);
                    minRow = Math.min(minRow, r);
                    maxRow = Math.max(maxRow, r);
                }
            }
        }

        int brickWidth = maxCol - minCol + 1;
        int brickHeight = maxRow - minRow + 1;
        int offsetCol = (4 - brickWidth) / 2;
        int offsetRow = (4 - brickHeight) / 2;

        for (int r = 0; r < nextShape.length; r++) {
            for (int c = 0; c < nextShape[r].length; c++) {
                if (nextShape[r][c] == 0) continue;

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(nextShape[r][c]));
                rectangle.setArcWidth(9);
                rectangle.setArcHeight(9);

                nextPanel.add(rectangle, c - minCol + offsetCol, r - minRow + offsetRow);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        rectangle.setStroke(Color.rgb(50, 50, 50, 0.7));
        rectangle.setStrokeWidth(1.2);
        rectangle.setStrokeType(StrokeType.INSIDE);
    }

    // Fall / Notification Pipeline

    private void moveDown(MoveEvent event) {
        if (!isInputEnabled()) {
            gamePanel.requestFocus();
            return;
        }

        DownData downData = eventListener.onDownEvent(event);

        if (downData.clearRow() != null && downData.clearRow().linesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.clearRow().scoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
        refreshBrick(downData.viewData());
        gamePanel.requestFocus();
    }

    // Public API used by GameController

    /**
     * Registers the . {@link InputEventListener} which receives translated
     * key events (move, rotate, drop, etc.).
     *
     * @param eventListener listener implementation, usually {@link GameController}
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Prepares the score label for binding. Currently only unbinds any
     * existing binding; the score is updated explicitly via {@link #updateScore(int)}.
     *
     * @param integerProperty unused at the moment, kept for backward compatibility
     */
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().unbind();
        }
    }

    /**
     * Called when the game has reached a terminal state.
     * <p>
     * Stops the timeline, shows the game-over panel and prompts the
     * player for a name before saving to the leaderboard
     */
    public void gameOver() {
        System.out.println("GAME OVER CALLED");

        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);

        Platform.runLater(() -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("Player");
            dialog.setTitle("Game Over");
            dialog.setHeaderText("Enter your name for the leaderboard:");
            dialog.setContentText("Name");

            String name = dialog.showAndWait().orElse("Unknown");

            String filename = gameController.getLeaderboardFile() + ".txt";
            LeaderboardManager.saveScore(filename, name, gameController.getScore());

            showLeaderboard();
            showGameOverMenu();
        });
    }

    private void showLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("leaderboard.fxml"));
            Parent root = loader.load();

            LeaderboardController controller = loader.getController();
            controller.loadLeaderboard(gameController.getLeaderboardFile());

            Stage stage = new Stage();
            stage.setTitle("Leaderboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not open leaderboard window.");
        }
    }

    /**
     * Resets the game state and restarts the main timeline.
     * Also clears the hold panel and hides the in-game menu.
     *
     * @param actionEvent optional event (ignored)
     */
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();

        // CLose the menu when restarting
        if (menuOverlay != null) {
            menuOverlay.setVisible(false);
        }

        holdPanel.getChildren().clear();

        gamePanel.requestFocus();
        timeLine.play();

        isPause.set(false);
        isGameOver.set(false);
        gameController.createNewGame();
    }

    /**
     * Called by {@link GameController} after construction so that the  GUI
     * can access game-specific data (score, leaderboard mode, etc.).
     *
     * @param controller active game controller
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
        Platform.runLater(() -> gamePanel.requestFocus());
    }

    /**
     * Binds the remaining time to the timer label and changes its colour
     * when the time is running low.
     *
     * @param timeRemaining property representing remaining seconds
     */
    public void bindTime(IntegerProperty timeRemaining) {
        if (timerLabel != null && timeRemaining != null) {
            timerLabel.textProperty().bind(timeRemaining.asString("Time: %d"));

            timeRemaining.addListener((observable, oldValue, newValue) -> {
                int time = newValue.intValue();

                if (time <= 10) {
                    timerLabel.setTextFill(Color.RED);
                } else {
                    timerLabel.setTextFill(Color.YELLOW);
                }
            });
        }
    }

    // Menu + Navigation
    @FXML
    private void openMenu() {
        isPause.set(true);
        if (timeLine != null) timeLine.pause();

        menuOverlay.setVisible(true);
    }

    @FXML
    private void menuResume() {
        menuOverlay.setVisible(false);
        isPause.set(false);

        if (timeLine != null) timeLine.play();
        gamePanel.requestFocus();
    }

    @FXML
    private void menuHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("startScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            StartController startController = loader.getController();
            startController.setPrimaryStage(stage);

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the score label text if it is present
     *
     * @param score current numeric score
     */
    public void updateScore(int score) {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
        }
    }

    /**
     * Makes the timer label visible for timed game modes.
     */
    public void enableTimerDisplay() {
        if (timerLabel != null) {
            timerLabel.setVisible(true);
        }
    }

    /**
     * Opens the leaderboard window for the current game mode.
     */
    @FXML
    private void openLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("leaderboard.fxml"));
            Parent root = loader.load();

            LeaderboardController controller = loader.getController();
            controller.loadLeaderboard(gameController.getLeaderboardFile());

            Stage stage = new Stage();
            stage.setTitle("Leaderboard");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showGameOverMenu() {
        if (menuOverlay != null) {
            menuOverlay.setVisible(true);
        }
    }
}