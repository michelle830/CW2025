package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 30;

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

    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Timeline timeLine;
    private GameController gameController;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private ActionEvent event;

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

    public void applyTheme(Theme theme) {
        String style;

        if (theme.getType() == Theme.Type.COLOR) {
            style = "-fx-background-color: " + theme.getValue() + ";";
        } else {
            style = "-fx-background-image: url('" + theme.getValue() + "');" + "-fx-background-size: cover;" + "-fx-background-position:center;";
        }

        if (rootPane != null) {
            rootPane.setStyle(style);
        }

        if (boardStack != null) {
            boardStack.setStyle("-fx-background-color: transparent;");
        }

        Platform.runLater(() -> gamePanel.requestFocus());
    }

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

    private void setupKeyboardHandling() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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
        });
    }

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
        return value == 0 ? Color.TRANSPARENT : Color.rgb(200, 200, 200, 0.3);
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.get()) {
            return;
        }

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

        int[][] ghostData = brick.getGhostData();
        ghostPanel.getChildren().clear();

        if (ghostData != null) {
            for (int r = 0; r < ghostData.length; r++) {
                for (int c = 0; c < ghostData[r].length; c++) {
                    if (ghostData[r][c] == 0) continue;

                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(getGhostFill(ghostData[r][c]));
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    rectangle.setStroke(Color.rgb(50, 50, 50, 0.7));
                    rectangle.setStrokeWidth(1.2);
                    rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

                    rectangle.setLayoutX(c * BRICK_SIZE);
                    rectangle.setLayoutY(r * BRICK_SIZE);

                    ghostPanel.getChildren().add(rectangle);
                }
            }

            ghostPanel.setTranslateX(brick.getghostX() * BRICK_SIZE + paddingOffset);
            ghostPanel.setTranslateY((brick.getghostY() - 2) * BRICK_SIZE);
        }

        refreshHoldPiece(brick.getHoldShape());
        refreshNextPiece(brick.getNextBrickData());

        if (scoreLabel != null && gameController != null) {
            scoreLabel.setText("Score: " + gameController.getScore());
        }
    }

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
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
    }

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

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().unbind();
        }
    }

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

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();

        holdPanel.getChildren().clear();

        gamePanel.requestFocus();
        timeLine.play();

        isPause.set(false);
        isGameOver.set(false);
        gameController.createNewGame();
    }

    public void setGameController(GameController controller) {
        this.gameController = controller;
        Platform.runLater(() -> gamePanel.requestFocus());
    }

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

    public void updateScore(int score) {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
        }
    }

    public void enableTimerDisplay() {
        if (timerLabel != null) {
            timerLabel.setVisible(true);
        }
    }

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