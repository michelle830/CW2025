package com.comp2042;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LeaderboardManager {

    private static final String BASE_PATH = "src/main/resources/";


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
