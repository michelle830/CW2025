package com.comp2042;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    /** New feature: called when user presses the Hold key (e.g. 'C') */
    void onHoldEvent();

    void createNewGame();

    ViewData onHardDropEvent();
}
