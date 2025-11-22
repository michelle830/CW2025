// Refactored SimpleBoard.java for COMP2042 Coursework
// Cleaned logic, reduced duplication, added helper methods, added documentation

package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import java.awt.Point;
import java.util.Arrays;

/**
 * Handles the core game logic for Tetris, including movement collision
 * detection, line clearing, score updates and brick spawning. Refactored for
 * COMP2042 to improve readability and maintainability.
 */

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private int [][] currentGameMatrix;

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;

    private Brick currentBrick;
    private Point offset; // Current brick position

    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.currentGameMatrix = new int[height][width];

        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.score = new Score();
    }

    /** Creates a new brick at spawn position */
    @Override
    public boolean createNewBrick() {
        this.currentBrick = brickGenerator.getNextBrick();
        brickRotator.setBrick(currentBrick);

        offset = new Point(width / 2 - 2, 0);
        int[][] shape = brickRotator.getCurrentShape();

        return tryMove(offset.x, offset.y, shape);
    }

    /**Attempts to move brick left */
    @Override
    public boolean moveBrickLeft() {
        return tryShift(-1,0);
    }

    /** Attempts to move brick right */
    @Override
    public boolean moveBrickRight() {
        return tryShift(1,0);
    }

    /** Attempts to move brick down: merges & spawns new brick on failure*/
    @Override
    public boolean moveBrickDown() {
        if (tryShift(0, 1)) return true;

        mergeBrick();
        int lines = clearFullLines();
        score.addLines(lines);

        return createNewBrick();
    }

    /** Generalised movement handler */
    private boolean tryShift(int dx, int dy) {
        int newX = offset.x + dx;
        int newY = offset.y + dy;
        int [][] shape = brickRotator.getCurrentShape();

        if (tryMove(newX, newY, shape)) {
            offset.move(newX, newY);
            return true;
        }
        return false;
    }

    /** Attempts rotation */
    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo next = brickRotator.getNextShape();
        if (tryMove(offset.x, offset.y, next.getShape())) {
            brickRotator.setCurrentShape(next.getShapeIndex());
            return true;
        }
        return false;
    }

    /** Collision + bounds detection */
    private boolean tryMove(int newX, int newY, int[][] shape) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    int boardX = newX + c;
                    int boardY = newY + r;

                    if (boardX < 0 || boardX >= width || boardY >= height) return false;
                    if (boardY >= 0 && currentGameMatrix[boardY][boardX] != 0) return false;
                }
            }
        }
        return true;
    }

    /** Merges current brick into background */
    @Override
    public void mergeBrickToBackground() {
        mergeBrick();
    }

    private void mergeBrick() {
        int [][] shape = brickRotator.getCurrentShape();
        for(int r = 0; r < shape.length; r++){
            for(int c = 0; c < shape[r].length; c++){
                if (shape[r][c] != 0) {
                    currentGameMatrix[offset.y + r][offset.x + c] = shape[r][c];
                }
            }
        }
    }

    /** Clears complete rows and shifts above rows downward. */
    *Override
    public ClearRow clearRows() {
        int lines = clearFullLines();
        return new ClearRow(lines);
    }

    private int clearFullLines() {
        int lines = 0;

        for (int y = 0; y < height; y++) {
            boolean full  = true;
            for (int x = 0; x < width; x++) {
                if (currentGameMatrix[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                lines++;
                for (int rr = y; rr > 0; rr--) {
                    currentGameMatrix[rr] = Arrays.copyOf(currentGameMatrix[rr - 1], width);
                }
                currentGameMatrix[0] = new int[width];
            }
        }
        return lines;
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(currentGameMatrix, brickRotator.getCurrentShape(), offset);
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}