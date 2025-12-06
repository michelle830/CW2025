package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * UI panel displayed when the game has ended.
 * <p>
 * The GameOverPanel is placed inside the game layout and becomes visible
 * only when {@link GuiController#gameOver()} is triggered. It displays a
 * large "GAME OVER" label styled through CSS and JavaFX effects.
 * This class is intentionally simple because all show/hide behaviour is
 * handled by {@link GuiController}.
 *
 * <h2> Refactoring Improvements</h2>
 * <ul>
 *     <li>Added complete Javadoc for coursework documentation</li>
 *     <li>Improved readability and formatting</li>
 *     <li>Ensured styling remains the same as original gameplay</li>
 * </ul>
 */


public class GameOverPanel extends BorderPane {

    /**
     * Creates a new {@code GameOverPanel} with a stylised
     * "GAME OVER" label centered in the pane.
     */
    public GameOverPanel() {
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        gameOverLabel.setFont(Font.font("System", FontWeight.BOLD, 48));
        gameOverLabel.setTextFill(Color.rgb(255,43,43));

        // Shadow effect for stronger visual feedback
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.8);
        gameOverLabel.setEffect(dropShadow);

        setCenter(gameOverLabel);
    }

}
