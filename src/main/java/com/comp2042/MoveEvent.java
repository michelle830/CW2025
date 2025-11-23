/**
 * Represents a movement request in the game
 *
 * A MoveEvent contains:
 * - The type of movement (LEFT, RIGHT, ROTATE, DOWN)
 * - The source of the movement  (USER key press of THREAD timer)
 *
 * This pbject is created by GuiContorller and processed by GameController
 *
 * Refactored for COMP2042 to improve clarity and documentation
 */
package com.comp2042;

public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent
     *
     * @param eventType type of movement(LEFT, RIGHT, DOWN, ROTATE)
     * @param eventSource source of the event (USER or THREAD)
     */

    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /** @return the type of movement requested */
    public EventType getEventType() {
        return eventType;
    }

    /** @return whether the event came from the user or game timer*/
    public EventSource getEventSource() {
        return eventSource;
    }
}
