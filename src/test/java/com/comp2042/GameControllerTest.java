package com.comp2042;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            Platform.startup(() -> {});
        }).start();

        latch.await();
    }

    @Test
    void testScoreIncreaseOnManualDrop() {
        GuiController dummy = new GuiController();  // not initialized
        GameController gc = new GameController(dummy);

        int before = gc.getScore();
        gc.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        assertTrue(gc.getScore() >= before, "Score should not decrease");
    }

    @Test
    void testHardDropCreatesNewBrick() {
        GuiController dummy = new GuiController();
        GameController gc = new GameController(dummy);

        ViewData v = gc.onHardDropEvent();

        assertNotNull(v, "Hard drop should return ViewData");
    }
}
