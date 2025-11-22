/**
 * Handles rotation logic for the active falling brick.
 *
 * Refactored for COMP2042 to:
 * -Improve readability
 * -Add documentation
 * -Remove unnecessary operations
 * -Keep rotation behaviour identical to original
 */

package com.comp2042;

import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    public NextShapeInfo getNextShape() {
        int next = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(next), next);
    }

    /**
     * Return the current brick shape matrix
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation index
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Assigns a brick to this rotator and resets rotation to default (0).
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        this.currentShape = 0;  // Reset rotation
    }


}
