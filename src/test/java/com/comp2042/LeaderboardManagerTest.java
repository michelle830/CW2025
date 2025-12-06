package com.comp2042;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardManagerTest {

    private static final String TEST_FILENAME = "test_leaderboard.txt";

    @BeforeAll
    static void setup() {
        // Set base path to a temporary test directory
        LeaderboardManager.setBasePathForTesting("target/test-resources/");

        try {
            Files.createDirectories(Paths.get("target/test-resources/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void cleanup() {
        try {
            Path testFile = Paths.get("target/test-resources/" + TEST_FILENAME);
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSaveAndLoadScores() {
        LeaderboardManager.saveScore(TEST_FILENAME,"Alice", 200);
        LeaderboardManager.saveScore(TEST_FILENAME,"Bob", 100);

        List<String> loaded = LeaderboardManager.loadScores(TEST_FILENAME);

        assertEquals(2, loaded.size(), "Should have 2 scores");
        assertEquals("Alice,200", loaded.get(0).trim(), "Highest score should be first");
        assertEquals("Bob,100", loaded.get(1).trim(), "Lowest score should be second");
    }

    @Test
    void testScoresSortedDescending() {
        LeaderboardManager.saveScore(TEST_FILENAME,"Charlie", 150);
        LeaderboardManager.saveScore(TEST_FILENAME,"Alice", 300);
        LeaderboardManager.saveScore(TEST_FILENAME,"Bob", 200);

        List<String> loaded = LeaderboardManager.loadScores(TEST_FILENAME);

        assertEquals(3, loaded.size());
        assertTrue(loaded.get(0).contains("300"));
        assertTrue(loaded.get(1).contains("200"));
        assertTrue(loaded.get(2).contains("150"));
    }

    @Test
    void testKeepsOnlyTop10() {
        for (int i = 1; i <= 12; i++) {
            LeaderboardManager.saveScore(TEST_FILENAME, "Player" + i, i * 10);
        }

        List<String> loaded = LeaderboardManager.loadScores(TEST_FILENAME);

        assertEquals(10, loaded.size(), "Should keep only top 10 scores");
        assertTrue(loaded.get(0).contains("120"), "Highest score should be 120");
    }

    @Test
    void testLoadNonExistentFile() {
        List<String> loaded = LeaderboardManager.loadScores("nonexistent.txt");

        assertTrue(loaded.isEmpty(), "Loading non-existent file should return empty list");
    }
}
