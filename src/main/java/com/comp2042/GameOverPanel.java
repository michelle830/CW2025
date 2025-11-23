/**
 * Simple UI component displayed when the game is over
 * It shows a centered "GAME OVER" label styled vis CSS
 * Refactored for COMP2042 to:
 * - Add documentation
 * - Improve readability
 */

package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
