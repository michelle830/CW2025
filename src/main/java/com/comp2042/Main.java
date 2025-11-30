package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load start screen first
        URL startURL = getClass().getClassLoader().getResource("startScreen.fxml");
        FXMLLoader loader = new FXMLLoader(startURL);
        Parent root = loader.load();

        // Get start controller
        StartController startController = loader.getController();
        startController.setPrimaryStage(primaryStage);

        // Show start screen
        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        System.out.println(getClass().getClassLoader().getResource("tetris.jpeg"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
