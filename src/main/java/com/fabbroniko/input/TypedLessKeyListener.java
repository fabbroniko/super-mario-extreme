package com.fabbroniko.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface TypedLessKeyListener extends KeyListener {

    @Override
    default void keyTyped(KeyEvent e) {
    }
}
