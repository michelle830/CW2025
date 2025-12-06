package com.comp2042;

import javafx.fxml.FXML;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    @Test
    void testScoreAdd() {
        Score score = new Score();
        score.add(10);
        assertEquals(10, score.scoreProperty().get());
    }

    @Test
    void testScoreReset() {
        Score score = new Score();
        score.add(50);
        score.reset();
        assertEquals(0, score.scoreProperty().get());
    }
}

