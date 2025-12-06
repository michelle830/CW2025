package com.comp2042;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all selectable visual themes for the Tetris game.
 * <p>
 * This class stores a predefined list of  {@link Theme} objects and allows the GUI
 * to cycle forward or backward through available themes. It behaves like a simple
 * circular list: when reaching the end, it wraps back to the start.
 *
 * <p>
 * This class is static-only and acts as a global theme provider for:
 * <ul>
 *     <li>{@code StartController} - for showing theme preview.</li>
 *     <li>{@code GuiController} - to apply themes during gameplay.</li>
 * </ul>
 *
 * @author Chan Michelle
 * @version 1.0
 */
public class ThemeManager {

    /** List of all available themes (colours and images). */
    private static final List<Theme> themes = new ArrayList<>();

    /** Current active theme index within the list. */
    private static int index = 0;

    //-------------------------------
    // INITIALISE AVAILABLE THEMES
    //-------------------------------
    static {
        // Colour-based themes
        themes.add(new Theme("Classic Black", Theme.Type.COLOR, "#000000"));
        themes.add(new Theme("Sky Blue", Theme.Type.COLOR, "#87CEEB"));
        themes.add(new Theme("Purple Glow", Theme.Type.COLOR, "#6A0DAD"));

        // Image-based themes
        themes.add(new Theme("Tetris 1", Theme.Type.IMAGE, "/themes/backgrounds/tetris.jpeg.jpeg"));
        themes.add(new Theme("Tetris 2", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png2.png"));
        themes.add(new Theme("Tetris 3", Theme.Type.IMAGE, "/themes/backgrounds/tetris.jpeg3.jpeg"));
        themes.add(new Theme("Tetris 4", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png4.png"));
        themes.add(new Theme("Tetris 5", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png5.png"));
        themes.add(new Theme("Tetris 6", Theme.Type.IMAGE, "/themes/backgrounds/tetris.jpeg6.png"));
        themes.add(new Theme("Tetris 7", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png7.png"));
    }

    /**
     * Returns the currently selected theme.
     *
     * @return the active {@link Theme}
     */
    public static Theme getCurrentTheme() {
        return themes.get(index);
    }

    /**
     * Moves to the next theme in the list.
     * Wraps back to index 0 when reaching the end
     *
     * @return the newly selected theme
     */
    public static Theme next() {
        index = (index + 1) % themes.size();
        return getCurrentTheme();
    }

    /**
     * Moves to the previous theme in the list.
     * Wraps to the last theme if currently at index 0.
     *
     * @return the newly selected theme
     */
    public static Theme previous() {
        index = (index - 1 + themes.size()) % themes.size();
        return getCurrentTheme();
    }
}
