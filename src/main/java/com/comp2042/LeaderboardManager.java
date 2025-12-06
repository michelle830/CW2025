package com.comp2042;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Manages saving and loading leaderboard scores.
 * <p>Scores are stored in plain text files under a base directory.
 * (default: {@code src/main/resources/}). Each score entry is recorded as:</p>
 *
 * <pre>
 * playerName, score
 * </pre>
 *
 * <p>Refactored for COMP2042 to:</p>
 * <ul>
 *     <li>Improve clarity and error handling</li>
 *     <li>Add full Javadoc documentation</li>
 *     <li>Allow JUnit to supply a custom file path</li>
 * </ul>
 *
 * @author Chan Michelle
 */
public class LeaderboardManager {

    /** Base file path where leaderboard files are stored */
    private static String BASE_PATH = "src/main/resources/";

    /**
     * Allows test cases to inject a custom file path.
     *
     * @param basePath custom base path for unit testing
     */
    static void setBasePathForTesting(String basePath) {
        if (basePath == null || basePath.isEmpty()) {
            throw new IllegalArgumentException("Base path must not be empty");
        }
        BASE_PATH = basePath.endsWith("/") ? basePath : basePath + "/";
    }

    /**
     * Saves a new score into the leaderboard file
     * <p>
     * The leaderboard is sorted from highest to lowest score
     * and only the top 10 scores are retained.
     * @param filename leaderboard file name
     * @param name     player name
     * @param score    player score
     */
    public static void saveScore(String filename, String name, int score) {
        try {
            Path path = Paths.get(BASE_PATH + filename);
            List<String> lines = new ArrayList<>();

            // Load existing scores if file exists
            if (Files.exists(path)) {
                lines.addAll(Files.readAllLines(path));
            }

            // Add new score
            lines.add(name + "," + score);

            // Sort high -> low
            lines.sort((a,b) -> {
                int scoreA = Integer.parseInt(a.split(",")[1].trim());
                int scoreB = Integer.parseInt(b.split(",")[1].trim());
                return Integer.compare(scoreB, scoreA);
            });

            // Keep top 10 only
            if (lines.size() > 10) {
                lines=lines.subList(0, 10);
            }

            // Save updated list
            Files.write(path, lines);

        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    /**
     * Loads scores from the given leaderboard file.
     *
     * @param filename leaderboard file name
     * @return list of stored scores, or empty list if file does not exist
     */
    public static List<String> loadScores(String filename) {
        try {
            Path path = Paths.get(BASE_PATH + filename);

            if (!Files.exists(path)) {
                return Collections.emptyList();
            }
            return Files.readAllLines(path);

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
