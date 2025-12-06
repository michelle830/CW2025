package com.comp2042;

/**
 * Container object that represents the result of a downward movement event.
 * <p>
 * It stores:
 * <ul>
 *     <li> The row-clear result (if any rows were cleared)</li>
 *     <li> The updated {@link ViewData} describing the brick after the movement</li>
 * </ul>
 * This object is used by {@link GuiController} to refresh the brick position
 * and display notifications when rows are cleared.
 */

public record DownData(ClearRow clearRow, ViewData viewData) {
    /**
     * Constructs a {@code DownData} result object
     *
     * @param clearRow result of row clearing (may be {@code null})
     * @param viewData updated brick view data
     */
    public DownData {
    }

    /**
     * @return the row-clear result, or null if no rows were removed
     */
    @Override
    public ClearRow clearRow() {
        return clearRow;
    }

    /**
     * @return the updated view data for drawing the active brick
     */
    @Override
    public ViewData viewData() {
        return viewData;
    }
}
