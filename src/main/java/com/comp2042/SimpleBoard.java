package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.Point;

/**
 * Core model representing the Tetris game board.
 * <p>
 * SimpleBoard is responsible for:
 * <ul>
 *     <li> Falling brick movement and rotation</li>
 *     <li> Collision detection</li>
 *     <li> Line clearing</li>
 *     <li> Score updates</li>
 *     <li> Ghost piece calculations</li>
 *     <li> Next-brick preview</li>
 *     <li> Hold/swap brick functionality</li>
 * </ul>
 *
 * <p>
 * Refactored for COMP2042 to improve readability,
 * maintainability and documentation quality while
 * preserving original gameplay behaviour.
 *
 * @author Chan Michelle
 * @version 1.2
 */

public class SimpleBoard implements Board {

    // FIELDS

    /** Width of the board in cells.*/
    private final int width;

    /** eight fo the board in cells.*/
    private final int height;

    /** Supplies new bricks and next-brick preview. */
    private final BrickGenerator brickGenerator;

    /** Handles rotation states for the current brick. */
    private final BrickRotator rotator;

    /** Score object that GUI binds to. */
    private final Score score;

    /** Static background grid of placed bricks */
    private int[][] boardMatrix;

    /** The active falling brick. */
    private Brick currentBrick;

    /** The current shape matrix of the falling brick. */
    private int[][] currentShape;

    /** Grid position of the falling brick. */
    private Point offset;

    /** Preview of the next brick */
    private int[][] nextBrickPreview;

    /** Stored hold brick (may be null). */
    private Brick holdBrick = null;

    /** Prevents holding twice during one turn. */
    private boolean holdUsedThisTurn = false;

    // CONSTRUCTORS

    /**
     * Creates a new board using a default RandomBrickGenerator.
     *
     * @param width board width in cells
     * @param height board height in cells
     */
    public SimpleBoard(int width, int height) {
        this(width, height, new RandomBrickGenerator());
    }

    /**
     * Creates a board with a custom brick generator.
     *
     * @param width            board width
     * @param height           board height
     * @param brickGenerator   custom brick supplier
     */
    public SimpleBoard(int width, int height, BrickGenerator brickGenerator) {
        this.width = width;
        this.height = height;
        this.boardMatrix = new int[height][width];
        this.brickGenerator = brickGenerator;
        this.rotator = new BrickRotator();
        this.score = new Score();
    }

    // HOLD / SWAP SYSTEM
    /** Hold or swap the current brick.
     * <p>
     * Rules:
     * <ul>
     *     <li> You can only hold once per turn.</li>
     *     <li> If no brick is held, the current one is stored and replaced.</li>
     *     <li> If a brick is already held, it is swapped with the current one.</li>
     * </ul>
     * */
    @Override
    public void holdCurrentBrick() {
        if (holdUsedThisTurn) {
            return;
        }

        if (holdBrick == null) {
            // First time holding:  store current brick and spawn a fresh one
            holdBrick = currentBrick;
            createNewBrick();
        } else {
            // Swap current brick with held one
            Brick temp = holdBrick;
            holdBrick = currentBrick;
            currentBrick = temp;

            rotator.setBrick(currentBrick);
            currentShape = rotator.getCurrentShape();

            // Standard spawn position for swapped piece
            offset = new Point (width / 2 - 2, 0);
        }

        holdUsedThisTurn = true;
    }

    // BRICK CREATION AND SPAWNING
    /**
     * Creates and spawns a new brick at the to of the board.
     *
     * @return {@code true} if the spawn location is blocked (game over),
     *          {@code false} otherwise.
     */
    @Override
    public boolean createNewBrick() {
        currentBrick = brickGenerator.getBrick();
        rotator.setBrick(currentBrick);
        currentShape = rotator.getCurrentShape();

       // Next brick preview
        Brick next = brickGenerator.getNextBrick();
        nextBrickPreview = (next != null) ? next.getShapeMatrix().get(0) : null;

        // Center horizontally
        offset = new Point(width / 2 - currentShape[0].length / 2, 0);
        holdUsedThisTurn = false;


        // Check spawn collision
        boolean blockedAtSpawn = !canPlace(offset.x, offset.y, currentShape);
        boolean blockedAbove = !canPlace(offset.x, offset.y - 1, currentShape);
        return blockedAtSpawn &&  blockedAbove;
    }

    //-----------
    // MOVEMENT
    //------------

    /**
     * Attempts to move the current brick one row down.
     * @return true if the brick moved successfully, false if movement is blocked.
     */
    @Override
    public boolean moveBrickDown() {
        if (canPlace(offset.x, offset.y + 1, currentShape)) {
            offset.translate(0,1);
            return true;
        }
        return false;
    }

    /**
     * Attempts to move the current brick one cell to the left.
     * @return true if movement was successful, false otherwise.
     */
    @Override
    public boolean moveBrickLeft() {
        if (canPlace(offset.x - 1, offset.y, currentShape)) {
            offset.translate(-1,0);
            return true;
        }
        return false;
    }

    /**
     * Attempts to move the current brick one cell to the right.
     * @return true if movement was successful, false otherwise.
     */
    @Override
    public boolean moveBrickRight() {
        if (canPlace(offset.x + 1, offset.y, currentShape)) {
            offset.translate(1,0);
            return true;
        }
        return false;
    }

    //----------------
    //ROTATION
    //-----------------
    /**
     * Attempts to rotate the current brick.
     * @return true if rotation was applied, false if rotation would collide / out-of-bounds.
     */
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

    //------------------------------
    // COLLISION CHECKING
    //------------------------------

    /** Determines whether a shape can fit at a given board position.
     *
     * @param x      left cell index
     * @param y      top cell index
     * @param shape  the tetromino matrix
     * @return       {@code true} if placement is valid
     */
    private boolean canPlace(int x, int y, int[][] shape) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {

                if (shape[r][c] == 0) continue;

                int bx = x + c;
                int by = y + r;

                // Out of bounds
                if (bx < 0 || bx >= width || by < 0 || by >= height) {
                    return false;
                }

                // Collision with background
                if (boardMatrix[by][bx] != 0) {
                    return false;
                }
            }
        }
        return true;
    }


    //------------------------------
    // GHOST PIECE CALCULATION
    //------------------------------

    /**
     * Finds where the current piece will land when dropped straight down.
     *
     * @param x  the x-position to test
     * @param startY  the starting y-position
     * @param shape   the brick shape matrix
     * @return        the final y-position after dropping
     */
    private int findGhostY(int x, int startY, int[][] shape) {
        int y = startY;
        while (canPlace(x, y + 1, shape)) {
            y++;
        }
        return y;
    }

    //--------------------------------
    // VIEW DATA (GUI SNAPSHOT)
    //--------------------------------

    /**
     * Builds a ViewData snapshot for the GUI:
     * - current falling brick
     * - ghost position
     * - next brick preview
     * - hold brick
     */
    @Override
    public ViewData getViewData() {
        int ghostY = findGhostY(offset.x, offset.y, currentShape);
        int ghostX = offset.x;

        // Hold brick data
        int[][] holdShapeMatrix = (holdBrick == null) ? null : holdBrick.getShapeMatrix().get(0);

        int[][] ghostMatrix = null;
        if (ghostY > offset.y) {
            ghostMatrix = MatrixOperations.copy(currentShape);
        } else {
            ghostMatrix = null;
        }

        return new ViewData(
                currentShape,
                offset.x,
                offset.y,
                nextBrickPreview,
                ghostMatrix,
                ghostX,
                ghostY,
                holdShapeMatrix
        );
    }

    //-------------------------
    // MERGING & ROW CLEARING
    //-------------------------

    /**
     * Merges the current brick into the static board matrix.
     */
    @Override
    public void mergeBrickToBackground() {
        for (int r = 0; r < currentShape.length; r++) {
            for (int c = 0; c < currentShape[r].length; c++) {

                if (currentShape[r][c] != 0) continue;

                int bx = offset.x + c;
                int by = offset.y + r;

                if (by >= 0 && by < height && bx >= 0 && bx < width) {
                    boardMatrix[by][bx] = currentShape[r][c];
                }
            }
        }
    }

    /**
     * Clears complete rows and shifts above rows downward.
     * @return ClearRow object describing how many lines were removed and the score bonus.
     */

    @Override
    public ClearRow clearRows() {
        ClearRow clear = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clear.newMatrix();
        return clear;
    }

    // ----------------------
    // ACCESSORS
    // ----------------------

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    @Override
    public Score getScore() {
        return score;
    }

    //--------------------------
    //GAME RESET
    //--------------------------
    /**
     * Resets the board state for a fresh game.
     * Score is reset and the hold system is cleared.
     */
    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();

         // Reset hold system
        holdBrick = null;
        holdUsedThisTurn = false;
    }
}