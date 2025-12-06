package com.comp2042;

/**
 * Represents a single movement request in this game.
 *
 * <p>A {@code MoveEvent} contains:</p>
 * <ul>
 *     <li>The type of movement (LEFT, RIGHT, ROTATE, DOWN)</li>
 *     <li>The source of the event (USER input or THREAD timer)</li>
 * </ul>
 *
 * <p>
 * These events are typically created by {@link GuiController} and then
 * processed by {@link GameController}.
 *
 * Refactored for COMP2042 to:
 * <ul>
 *     <li>Fix documentation errors</li>
 *     <li>Add Javadoc for enums</li>
 *     <li>Ensure immutability and clarity</li>
 * </ul>
 *
 * @author Chan Michelle
 * @version 1.0
 */

public final class MoveEvent {

    /** The kind of movement requested. */
    private final EventType eventType;

    /** The origin of the request (user key press or time tick). */
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent
     *
     * @param eventType   Type of movement
     * @param eventSource Source of the movement
     */

    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /** @return the type of movement requested */
    public EventType getEventType() {
        return eventType;
    }

    /** @return The source of the event*/
    public EventSource getEventSource() {
        return eventSource;
    }
}
