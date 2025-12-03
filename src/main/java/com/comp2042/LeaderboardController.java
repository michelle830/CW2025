package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardController {

    @FXML
    private VBox scoreList;

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) scoreList.getScene().getWindow();
        stage.close();
    }

    public void loadLeaderboard(String mode) {
        String filename = mode + ".txt";

        List<String> scores = LeaderboardManager.loadScores(filename);

        scoreList.getChildren().clear();

        if (scores.isEmpty()) {
            scoreList.getChildren().add(new Label("No scores yet!"));
            return;
        }

        int rank = 1;
        for (String entry : scores) {
            String[] parts = entry.split(",");
            String name = parts[0];
            String score = parts[1];

            Label label = new Label(rank + "." + name + " - " + score);
            label.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
            scoreList.getChildren().add(label);

            rank++;
        }
    }
}
