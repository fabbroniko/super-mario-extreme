package com.fabbroniko.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface UIKeyListener extends KeyListener {

    @Override
    default void keyTyped(KeyEvent e) {
    }

    @Override
    default void keyPressed(KeyEvent e) {
    }
}
