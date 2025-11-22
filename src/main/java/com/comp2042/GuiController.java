/**
 * Manages all JavaFX UI components such as the board grid,
 * next-brick preview and score display. It also listens to
 * keyboard input and forwards movement events to the game
 * controller via the InputEventListener.
 *
 * Refactored for COMP2042 to:
 * - Improve readability
 * -Reduce duplication in key handling and drawing
 */


package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    /** Background grid rectangles representing the placed bricks */
    private Rectangle[][] displayMatrix;

    /**Current falling brick rectangles*/
    private Rectangle[][] rectangles;

    private InputEventListener eventListener;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        setupKeyboardHandling();

        gameOverPanel.setVisible(false);
    }

    /**
     * Sets up keyboard input handling for the main game panel.
     * Forwards events to the InputEventListener if game is not paused or over
     */
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

                    default:
                        break;

                }
            }
        });
    }

    /**
     * Returns true if the game is not paused and not over,
     */
    private boolean isInputEnabled() {
        return Boolean.FALSE.equals(isPause.getValue()) && Boolean.FALSE.equals(isGameOver.getValue());
    }

    /**
     * Initialises the game view: builds the background grid and the
     * current brick representation, and starts the automatic downward
     * movement timer.
     *
     * @param boardMatrix the current background matrix
     * @param brick the initial brick view data
     */


    public void initGameView(int[][] boardMatrix, ViewData brick) {
        initBackgroundGrid(boardMatrix);
        initBrickPanel(brick);
        positionBrickPanel(brick);

        setupTimeLine();
    }

    /**
     * Creates the background grid rectangles for the board.
     */
    private void initBackgroundGrid(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j ,i-2);
            }
        }
    }

    /**
     * Creates the rectangles representing the active falling brick.
     */
    private void initBrickPanel(ViewData brick) {
        int[][] brickData = brick.getBrickData();
        rectangles =  new Rectangle[brickData.length][brickData[0].length];

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brickData[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    /**
     * Position the brick panel according to the brick's current coordinates.
     */
    private void positionBrickPanel(ViewData brick) {
        brickPanel.setLayoutX(
                gamePanel.getLayoutX() + brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
    }

    /**
     * Sets up the game timer for automatic downward movement.
     */
    private void setupTimeLine() {
        timeLine = new Timeline(new KeyFrame(Duration.millis(400), ae -> moveDown{new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setcycleCount(Timeline.INDEFINITE);
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
                return  Color.YELLOW;
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

        /**
         * Updates the brick panel position and colours according to the given ViewData.
         */


    private void refreshBrick(ViewData brick){
            if (Boolean.TRUE.equals(isPause.getValue())) {
                return;
            }

            positionBrickPanel(brick);

            int[][] brickData = brick.getBrickData();
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    setRectangleData(brickData[i][j], rectangles[i][j]);
                }
            }
        }

        /**
         * Redraws the background grid using the given board matrix.
         */

        public void refreshGameBackground(int[][] board){
            for (int i = 2; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    setRectangleData(board[i][j], displayMatrix[i][j]);
                }
            }
        }

        /**
         * Applies colour and rounded corners to a rectangle
         */
        private void setRectangleData(int color, Rectangle rectangles) {
            rectangles.setFill(getFillColor(color));
            rectangles.setArcHeight(9);
            rectangles.setArcWidth(9);
        }

        /**
         * Handles a downward movement event (user or timer)
         */

        private void moveDown(MoveEvent event) {
            if (!isInputEnabled()) {
                gamePanel.requestFocus();
                return;
            }

            if (isPause.getValue() == Boolean.FALSE) {
                DownData downData = eventListener.onDownEvent(event);
                if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                    NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                    groupNotification.getChildren().add(notificationPanel);
                    notificationPanel.showScore(groupNotification.getChildren());
                }

                refreshBrick(downData.getViewData());
                gamePanel.requestFocus();
            }

            public void setEventListener(InputEventListener eventListener) {
                this.eventListener = eventListener;
            }

            public void bindScore(IntegerProperty integerProperty) {
            }

            public void gameOver() {
                timeLine.stop();
                gameOverPanel.setVisible(true);
                isGameOver.setValue(Boolean.TRUE);
            }

            /**
             * Starts a completely new game, resetting flags and restarting the timer.
             */
            public void newGame(ActionEvent actionEvent) {
                timeLine.stop();
                gameOverPanel.setVisible(false);

                eventListener.createNewGame();

                gamePanel.requestFocus();
                timeLine.play();

                isPause.setValue(Boolean.FALSE);
                isGameOver.setValue(Boolean.FALSE);
            }

            public void pauseGame(ActionEvent actionEvent) {
                gamePanel.requestFocus();
            }



    }








