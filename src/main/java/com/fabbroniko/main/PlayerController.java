package com.fabbroniko.main;

import java.awt.event.KeyListener;

public interface PlayerController {

    void addKeyListener(final KeyListener KeyListener);

    void removeKeyListener(final KeyListener KeyListener);
}
