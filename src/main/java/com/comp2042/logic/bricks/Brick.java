package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Represents a Tetris Tetromino
 * <p>
 * Each {@code Brick} implementation provides a list of rotation
 * matrices for a given Tetromino. Every matrix is a 4x4 grid where
 * non-zero values represent filled cells
 *
 * @author Chan Michelle
 * @version 1.0
 */

public interface Brick {

    /**
     * Returns all rotation states of this brick.
     *
     * @return list of 2D matrices representing each rotation of this brick
     */

    List<int[][]> getShapeMatrix();
}
