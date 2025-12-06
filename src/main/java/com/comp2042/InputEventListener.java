package com.comp2042;

/**
 * Interface that defines all user and game-triggered input events.
 * <p>
 * This interface acts as the communication contract between the
 * {@link GuiController} and the {@link GameController}.
 * The GUI translates keyboard input into high-level {@link MoveEvent}s,
 * which are then passed into these handler methods.
 *
 * <p>
 * This design enforces a clean MVC separation:
 * <ul>
 *     <li>GUI handles only user interaction</li>
 *     <li>GameController processes all logic</li>
 * </ul>
 */
public interface InputEventListener {

    /**
     * Processes a downward movement request.
     * <p>
     * This is triggered by:
     * <ul>
     *     <li>User pressing DOWN key</li>
     *     <li>Automatic game gravity using a {@link javafx.animation.Timeline}</li>
     * </ul>
     * @param event the {@link MoveEvent} describing the action source
     * @return {@link DownData} containing the updated game state
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Processes a left movement request.
     *
     * @param event the {@link MoveEvent} describing the movement
     * @return updated {@link ViewData} after movement
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Processes a right movement request.
     *
     * @param event the {@link MoveEvent} describing the movement
     * @return updated {@link ViewData} after movement
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Processes a rotation request
     *
     * @param event the {@link MoveEvent} describing the rotation
     * @return updated {@link ViewData} after rotation
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Processes a hard drop request.
     * <p>
     * This instantly drops the active brick to the lowest valid position.
     * @return updated {@link ViewData} after the drop completes
     */
    ViewData onHardDropEvent();

    /**
     * Processes the hold action.
     * <p>
     * Stores the current brick into the hold slot and swaps it
     * with the previously held brick (if available).
     */
    void onHoldEvent();

    /**
     * Creates and initialises a new game session.
     * <p>
     * This resets:
     * <ul>
     *     <li>Board state</li>
     *     <li>Score</li>
     *     <li>Current brick</li>
     *     <li>Hold slot</li>
     *     <li>Next preview</li>
     * </ul>
     */
    void createNewGame();
}
