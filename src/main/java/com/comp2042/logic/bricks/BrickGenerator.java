package com.comp2042.logic.bricks;


/**
 * Abstraction for a brick provides used by the {@code SimpleBoard}.
 * <p>
 * Implementations supply the current active brick as well as
 * a "next" brick for preview
 *
 * @author Chan Michelle
 * @version 1.0
 */
public interface BrickGenerator {

    /**
     * Returns the next brick to be used as the active falling brick.
     *
     * @return current active {@link Brick}
     */

    Brick getBrick();

    /**
     * Returns the upcoming brick for preview purposes.
     *
     * @return non-active {@link Brick} that will be used after the current one
     */

    Brick getNextBrick();
}
