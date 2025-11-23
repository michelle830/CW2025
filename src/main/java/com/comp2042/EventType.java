/**
 * Represents the type of movement or action requested by the player
 * or triggered by the game engine
 * DOWN - move the active brick downward
 * LEFT - move the brick left
 * RIGHT -move the brick right
 * ROTATE - rotate the brick
 * Refactored for COMP2042 to add clarity and documentation
 */

package com.comp2042;

public enum EventType {
    DOWN, LEFT, RIGHT, ROTATE
}
