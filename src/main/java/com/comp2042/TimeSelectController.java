package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class TimeSelectController {

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private void startTimedGame(int seconds) {
        try {
            URL gameURL = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader loader = new FXMLLoader(gameURL);

            Parent gameRoot = loader.load();

            GuiController guiController = loader.getController();
            guiController.applyTheme(ThemeManager.getCurrentTheme());
            guiController.enableTimerDisplay();
            GameController gameController = new GameController(guiController, seconds);
            guiController.setGameController(gameController);

            primaryStage.setScene(new Scene(gameRoot));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void start1min() { startTimedGame(60);}
    @FXML private void start2min() { startTimedGame(120);}
    @FXML private void start3min() { startTimedGame(180);}
}
