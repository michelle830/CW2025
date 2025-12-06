package com.comp2042;

/**
 * Represents a selectable visual theme for the Tetris game.
 * <p>
 * A theme defines how the game's background is displayed, either through:
 * <ul>
 *     <li>A solid colour value (e.g., '#000000")</li>
 *     <li>An image path (e.g., "/themes/backgrounds/tetris.jpeg")</li>
 * </ul>
 * This class is immutable, meaning theme objects cannot be changed once created.
 *
 * @author Chan Michelle
 * @version 1.0
 */

public class Theme {

    /**
     * Defines the type of theme:
     * <ul>
     *     <li>{@code COLOR} - The theme uses a colour value such as "#87CEEB".</li>
     *     <li>{@code IMAGE} - The theme uses a background image path.</li>
     * </ul>
     */
    public enum Type {COLOR, IMAGE}

    /** Display name of the theme (e.g.,"Classic Black", "Tetris 1"). */
    private final String name;

    /** Whether the theme is a colour or image. */
    private final Type type;

    /** The actual colour code or image path. */
    private final String value;

    /**
     * Constructs a Theme object.
     *
     * @param name human-readable theme name
     * @param type the type of theme (COLOR or IMAGE)
     * @param value the colour hex code or image resource path
     */
    public Theme(String name, Type type , String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * @return the name of this theme
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type of this them (COLOR or IMAGE)
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the colour value or image path associated with this theme
     */
    public String getValue() {
        return value;
    }
}
