package com.comp2042;

import com.comp2042.logic.bricks.Brick;

/**
 * Handles rotation logic for the active falling brick.
 * <p>
 * Refactored for COMP2042 to:
 * <ul>
 *     <li> Improve readability</li>
 *     <li> Add documentation</li>
 *     <li> Keep rotation behaviour identical to the original implementation</li>
 * </ul>
 *
 * @author Chan Michelle
 * @version 1.0
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates the next rotation state without mutating the internal state.
     *
     * @return {@link NextShapeInfo} representing the next rotation
     */
    public NextShapeInfo getNextShape() {
        int next = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(next), next);
    }

    /**
     * Return the current brick shape matrix
     *
     * @return current rotation matrix
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation index
     *
     * @param currentShape index of the rotation state to use
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Assigns a brick to this rotator and resets rotation index to {@code 0}.
     *
     * @param brick new active {@link Brick}
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        this.currentShape = 0;  // Reset rotation
    }
}
