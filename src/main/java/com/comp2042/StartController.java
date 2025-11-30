package com.comp2042;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class StartController {

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void startGame() {
        try {
            URL gameURL = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader loader = new FXMLLoader(gameURL);

            Parent gameRoot = loader.load();

            // Get GUI controller from gameLayout
            GuiController guiController = loader.getController();
            GameController gameController = new GameController(guiController);
            guiController.setGameController(gameController);

            primaryStage.setScene(new Scene(gameRoot));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showInstructions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("instructions.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Instructions");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
