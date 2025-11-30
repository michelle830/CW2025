package com.comp2042;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LeaderboardManager {

    private static final String FILE_PATH = "src/main/resources/leaderboard.txt";

    public static void saveScore(String name, int score) {
        try {
            List<String> lines = new ArrayList<>();

            // Load old scores
            if (Files.exists(Paths.get(FILE_PATH))) {
                lines.addAll(Files.readAllLines(Paths.get(FILE_PATH)));
            }

            // Add new score
            lines.add(name + "," + score);

            // Sort highest first
            lines.sort((a, b) -> {
                int scoreA = Integer.parseInt(a.split(",")[1]);
                int scoreB = Integer.parseInt(b.split(",")[1]);
                return Integer.compare(scoreB, scoreA);
            });

            // Keep only top 10
            if (lines.size() >10) {
                lines = lines.subList(0, 10);
            }

            Files.write(Paths.get(FILE_PATH), lines);

        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    public static List<String> loadScores() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                return Collections.emptyList();
            }
            return Files.readAllLines(Paths.get(FILE_PATH));
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
