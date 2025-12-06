package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Default implementation of {@link BrickGenerator} used by the Tetris board.
 * <p>
 * Maintains an internal list of brick types and a small queue for
 * "next brick" preview support
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Creates a new random brick generator with all seven Tetromino types.
     */
    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        // Pre-fill queue with two random bricks
        nextBricks.add(randomBrick());
        nextBricks.add(randomBrick());
    }

    private Brick randomBrick() {
        return brickList.get(ThreadLocalRandom.current().nextInt(brickList.size()));
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(randomBrick());
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
