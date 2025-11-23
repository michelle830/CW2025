/**
 * Container obeject that represents the result of a downward movement event
 *
 * It stores:
 * - The row-clear result (if any rows were cleared)
 * - The update ViewData describing the brick after the movement
 *
 * This object is used by  GuiController to refresh the brick position
 * and display notifications when rows are cleared.
 *
 * Refactored for COMP2042 to improve clarity and documentation
 */


package com.comp2042;

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a DownDate result object
     *
     * @param clearRow result of row clearing (may be null)
     * @param viewData updated brick view data
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * @return the row-clear result, or null if now rpws were removed
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * @return the updated view data for drawing the active brick
     */
    public ViewData getViewData() {
        return viewData;
    }
}
