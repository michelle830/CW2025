package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;

public class StartController {

    private Stage primaryStage;

    @FXML private StackPane themePreview;

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
            guiController.applyTheme(ThemeManager.getCurrentTheme());
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

    @FXML
    private void chooseTimeMode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("timeSelect.fxml"));
            Parent root = loader.load();

            TimeSelectController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        updateThemePreview();
    }

    @FXML
    private void nextTheme() {
        ThemeManager.next();
        updateThemePreview();
    }

    @FXML
    private void previousTheme() {
        ThemeManager.previous();
        updateThemePreview();
    }

    private void updateThemePreview() {
        Theme theme = ThemeManager.getCurrentTheme();

        if (theme.getType() == Theme.Type.COLOR) {
            themePreview.setStyle("-fx-background-color: " + theme.getValue() + ";" + "-fx-border-color: white; -fx-border-width: 2;");
        } else {
            themePreview.setStyle("-fx-background-image: url('" + theme.getValue() + "');" + "-fx-background-size: cover;" + "-fx-background-position: center;" + "-fx-border-color: white; -fx-border-width: 2;");
        }
    }
}

