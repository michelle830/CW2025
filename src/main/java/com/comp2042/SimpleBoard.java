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

    private Brick holdBrick = null;
    private boolean holdUsedThisTurn = false;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;

        this.boardMatrix = new int[height][width];
        this.brickGenerator = new RandomBrickGenerator();
        this.rotator = new BrickRotator();
        this.score = new Score();
    }

    /** Hold / swap the current brick*/
    @Override
    public void holdCurrentBrick() {
        if (holdUsedThisTurn) {
            return;
        }

        if (holdBrick == null) {
            holdBrick = currentBrick;
            createNewBrick();
        } else {
            Brick temp = holdBrick;
            holdBrick = currentBrick;
            currentBrick = temp;

            rotator.setBrick(currentBrick);
            currentShape = rotator.getCurrentShape();
            offset = new Point (width / 2 - 2, 0);
        }

        holdUsedThisTurn = true;
    }

    /**
     * Creates a new brick at spawn position
     * @ return true if the brick cannot be placed (game over).
     */
    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick();
        rotator.setBrick(currentBrick);
        currentShape = rotator.getCurrentShape();

       // Prepare preview (next brick)
        Brick next = brickGenerator.getNextBrick();
        nextBrickPreview = (next != null) ? next.getShapeMatrix().get(0) : null;

        // Spawn in TOP HIDDEN ROWS
        offset = new Point(width / 2 - currentShape[0].length / 2, 0);
        holdUsedThisTurn = false;


        // GAME OVER check (try slightly higher for long shapes)
        boolean blocked = !canPlace(offset.x, offset.y, currentShape) && !canPlace(offset.x, offset.y - 1, currentShape);

        return blocked;

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

    /** Collision + bounds check for a shape at (x,y) ont eh board */
    private boolean canPlace(int x, int y, int[][] shape) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 0) {
                    continue;
                }

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

    private int findGhostY(int x, int startY, int[][] shape) {
        int ghostY = startY;

        while (canPlace(x, ghostY + 1, shape)) {
            ghostY++;
        }

        return ghostY;
    }

    @Override
    public ViewData getViewData() {
        int ghostY = findGhostY(offset.x, offset.y, currentShape);
        int ghostX = offset.x;

        int[][] holdShapeMatrix = (holdBrick == null) ? null : holdBrick.getShapeMatrix().get(0);

        int[][] ghostMatrix = null;
        if (ghostY > offset.y) {
            ghostMatrix = currentShape;
        } else if (ghostY == offset.y) {
            ghostMatrix = null;
        }

        return new ViewData(currentShape,
                offset.x,
                offset.y,
                nextBrickPreview,
                ghostMatrix,
                ghostX,
                ghostY,
                holdShapeMatrix);
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

    private  int getLeftOffset(int[][] shape) {
        for (int col = 0; col < shape[0].length; col++) {
            for (int row = 0; row < shape.length; row++) {
                if (shape[row][col] != 0) {
                    return col;
                }
            }
        }
        return 0;
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
         // Reset hold system
        holdBrick = null;
        holdUsedThisTurn = false;
    }

    public int[][] getHoldShape() {
        if (holdBrick == null) return null;
        return holdBrick.getShapeMatrix().get(0);
    }




}