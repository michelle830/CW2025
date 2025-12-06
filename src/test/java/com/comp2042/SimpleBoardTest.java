package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleBoardTest {

    @Test
    void testCreateNewBrickNotGameOver() {
        SimpleBoard board = new SimpleBoard(10, 25);
        boolean gameOver = board.createNewBrick();

        assertFalse(gameOver);
        assertNotNull(board.getViewData());
    }

    @Test
    void testMoveBrickDown() {
        SimpleBoard board = new SimpleBoard(10, 25);
        board.createNewBrick();

        boolean moved = board.moveBrickDown();
        assertTrue(moved, "Brick should move down");
    }

    @Test
    void testMoveBrickLeftRight() {
        SimpleBoard board = new SimpleBoard(10, 25);
        board.createNewBrick();

        assertTrue(board.moveBrickLeft());
        assertTrue(board.moveBrickRight());
    }

    @Test
    void testHoldBrick() {
        SimpleBoard board = new SimpleBoard(10, 25);
        board.createNewBrick();

        board.holdCurrentBrick();
    }

    @Test
    void testLineClear() {
        SimpleBoard board = new SimpleBoard(10, 25);

        // Fill bottom row
        int[][] matrix = board.getBoardMatrix();
        for (int i = 0; i < 10; i++) {
            matrix[24][i] = 1;
        }

        ClearRow cr = board.clearRows();
        assertEquals(1, cr.linesRemoved());
        assertEquals(50, cr.scoreBonus());
    }
}
