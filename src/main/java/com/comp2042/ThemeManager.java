package com.comp2042;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private static final List<Theme> themes = new ArrayList<>();
    private static int index = 0;

    static {

        themes.add(new Theme("Classic Black", Theme.Type.COLOR, "#000000"));
        themes.add(new Theme("Sky Blue", Theme.Type.COLOR, "#87CEEB"));
        themes.add(new Theme("Purple Glow", Theme.Type.COLOR, "#6A0DAD"));

        themes.add(new Theme("Tetris 1", Theme.Type.IMAGE, "/themes/backgrounds/tetris.jpeg.jpeg"));
        themes.add(new Theme("Tetris 2", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png2.png"));
        themes.add(new Theme("Tetris 3", Theme.Type.IMAGE, "/themes/backgrounds/tetris.jpeg3.jpeg"));
        themes.add(new Theme("Tetris 4", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png4.png"));
        themes.add(new Theme("Tetris 5", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png5.png"));
        themes.add(new Theme("Tetris 6", Theme.Type.IMAGE, "/themes/backgrounds/tetris.jpeg6.png"));
        themes.add(new Theme("Tetris 6", Theme.Type.IMAGE, "/themes/backgrounds/tetris.png7.png"));
    }

    public static Theme getCurrentTheme() {
        return themes.get(index);
    }

    public static Theme next() {
        index = (index + 1) % themes.size();
        return getCurrentTheme();
    }

    public static Theme previous() {
        index = (index - 1 + themes.size()) % themes.size();
        return getCurrentTheme();
    }
}
