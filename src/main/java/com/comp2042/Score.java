/**
 * Represents the player's score for the Tetris game.
 *
 * Stores score as a JavaFX IntegerProperty so the GUI can bind to it
 * and automatically updates whenever the score changes.
 *
 * Refactored for COMP2042 to:
 * - Improve documentation
 * - Clarify intent and usage
 * - Follow clean-code conventions
 */

package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    /** JavaFX property storing the player's current score */
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * @return the score property, allowing UI elements to bind to it
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds the given amount to the player's current score.
     *
     * @param amount value to add
     */
    public void add(int amount){
        score.set(score.get() + amount);
    }

    /**
     * Resets the score back to zero.
     */
    public void reset() {
        score.set(0);
    }
}
