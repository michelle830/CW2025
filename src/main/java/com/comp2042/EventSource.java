package com.comp2042;

/**
 * Indicates the origin of a movement event.
 *
 * <ul>
 *     <li>{@link #USER} - triggered by a key press from the player</li>
 *     <li>{@link #THREAD} - triggered by the automatic game timer</li>
 * </ul>
 */
public enum EventSource {
    USER, THREAD
}
