package com.comp2042;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeManagerTest {

    @Test
    void testThemeSwitching() {
        Theme t1 = ThemeManager.getCurrentTheme();
        Theme t2 = ThemeManager.next();
        Theme t3 = ThemeManager.previous();

        assertNotNull(t1);
        assertNotNull(t2);
        assertNotNull(t3);
    }
}
