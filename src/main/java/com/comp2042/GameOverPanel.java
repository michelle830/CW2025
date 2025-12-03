/**
 * Simple UI component displayed when the game is over
 * It shows a centered "GAME OVER" label styled vis CSS
 * Refactored for COMP2042 to:
 * - Add documentation
 * - Improve readability
 */

package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        gameOverLabel.setFont(Font.font("System", FontWeight.BOLD, 48));
        gameOverLabel.setTextFill(Color.rgb(255,43,43));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.8);
        gameOverLabel.setEffect(dropShadow);

        setCenter(gameOverLabel);
    }

}
