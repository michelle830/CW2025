package com.comp2042;

import com.comp2042.logic.bricks.IBrick;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BrickRotationTest {

    @Test
    void testRotationMatrixStaysImmutable() {
        IBrick brick = new IBrick();
        int[][] shape1 = brick.getShapeMatrix().get(0);
        int[][] shape2 = brick.getShapeMatrix().get(1);

        assertNotSame(shape1, shape2);
        assertNotEquals(shape1, shape2);
    }
}
