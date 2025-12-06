package com.comp2042;

/**
 * Represents the type of movement or action requested by the player
 * or triggered by the game engine
 */

public enum EventType {
    /** Move the active brick downward by one row. */
    DOWN,
    /** Move the active brick one cell to the left. */
    LEFT,
    /** Move the active brick one cell to the right. */
    RIGHT,
    /** Rotate the active brick . */
    ROTATE
}
