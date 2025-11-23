// Refactored SimpleBoard.java for COMP2042 Coursework
// Cleaned logic, reduced duplication, added helper methods, added documentation

package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import java.awt.Point;


/**
 * Handles the core game logic for Tetris, including movement collision
 * detection, line clearing, score updates and brick spawning. Refactored for
 * COMP2042 to improve readability and maintainability.
 */

public class SimpleBoard implements Board {

    private final int width;
    private final int height;

    private final BrickGenerator brickGenerator;
    private final BrickRotator rotator;
    private final Score score;

    private int[][] boardMatrix;

    private Brick currentBrick;
    private int[][] currentShape;
    private Point offset;

    private int[][] nextBrickPreview;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;

        this.boardMatrix = new int[height][width];
        this.brickGenerator = new RandomBrickGenerator();
        this.rotator = new BrickRotator();
        this.score = new Score();
    }

    /**
     * Creates a new brick at spawn position
     */
    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick();
        rotator.setBrick(currentBrick);
        currentShape = rotator.getCurrentShape();

       // Prepare preview (next brick)
        Brick next = brickGenerator.getNextBrick();
        nextBrickPreview = (next != null) ? next.getShapeMatrix().get(0) : null;

        offset = new Point(width / 2 - 2, 0);

        // GAME OVER check
        return !canPlace(offset.x, offset.y, currentShape);
    }

    @Override
    public boolean moveBrickDown() {
        if (canPlace(offset.x, offset.y + 1, currentShape)) {
            offset.translate(0,1);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBrickLeft() {
        if (canPlace(offset.x - 1, offset.y, currentShape)) {
            offset.translate(-1,0);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBrickRight() {
        if (canPlace(offset.x + 1, offset.y, currentShape)) {
            offset.translate(1,0);
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo next = rotator.getNextShape();
        int[][] rotatedShape = next.getShape();

        if (canPlace(offset.x, offset.y, rotatedShape)) {
            rotator.setCurrentShape(next.getPosition());
            currentShape = rotatedShape;
            return true;
        }
        return false;
    }

    private boolean canPlace(int x, int y, int[][] shape) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 0) continue;

                int bx = x + c;
                int by = y + r;

                if (bx < 0 || bx >= width || by < 0 || by >= height) {
                    return false;
                }

                if (boardMatrix[by][bx] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Merges current brick into background
     */
    @Override
    public void mergeBrickToBackground() {
        for (int r = 0; r < currentShape.length; r++) {
            for (int c = 0; c < currentShape[r].length; c++) {
                if (currentShape[r][c] != 0) {
                    int bx = offset.x + c;
                    int by = offset.y + r;
                    if (by >= 0 && by < height && bx >= 0 && bx < width) {
                        boardMatrix[by][bx] = currentShape[r][c];
                    }
                }
            }
        }
    }

    /**
     * Clears complete rows and shifts above rows downward.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clear = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clear.newMatrix();
        return clear;
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(currentShape, offset.x, offset.y, nextBrickPreview);
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}