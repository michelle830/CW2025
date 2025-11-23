/**
 * A temporary popup panel used to display score bonuses (e.g., "+40)
 * when rows ar cleared in the game.
 *
 * The panel:
 * - Shows a glowing score label
 * - Moves upward slightly
 * - Fades out
 * - Removes itself from the parent node list after animation ends
 *
 * Refactored for COMP2042 to improve readability, documentation and structure.
 */
package com.comp2042;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NotificationPanel extends BorderPane {

    /**
     * Creates a notification panel with glowing score text
     *
     * @param text the text to display , usually a bonus like "+40"
     */

    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);

        Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        score.setTextFill(Color.WHITE);

        Effect glow = new Glow(0.6);
        score.setEffect(glow);

        setCenter(score);

    }

    /**
     * Plays upward movement + fade-out animation, then removes the panel.
     *
     * @param parentChildrenList the group of chilren to remove from once finished
     */

    public void showScore(ObservableList<Node> parentChildrenList) {

        // Fade out over 2 seconds
        FadeTransition fadeTransition= new FadeTransition(Duration.millis(2000), this);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        // Move upward over 2.5 seconds
        TranslateTransition moveTransition = new TranslateTransition(Duration.millis(2500), this);
        moveTransition.setToY(getLayoutY() - 40);

        // Play both transitions together
        ParallelTransition combinedTransition = new ParallelTransition(moveTransition, fadeTransition);

        // Remove the panel from the parent after animation finishes
        combinedTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parentChildrenList.remove(NotificationPanel.this);
            }
        });
        combinedTransition.play();
    }
}
