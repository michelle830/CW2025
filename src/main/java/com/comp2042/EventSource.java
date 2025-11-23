/**
 * Indicates the origin of a movement event
 *
 * USER - the player pressed a key
 * THREAD - the automatic game timer triggered the movement
 *
 * Refactored for COMP2042 to add clatiry and documentation
 */
package com.comp2042;

public enum EventSource {
    USER, THREAD
}
