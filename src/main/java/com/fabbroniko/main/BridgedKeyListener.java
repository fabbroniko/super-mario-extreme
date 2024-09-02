package com.fabbroniko.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BridgedKeyListener implements CustomKeyListener {

    private KeyListener keyListener;

    @Override
    public void setKeyListener(final KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (keyListener != null) {
            keyListener.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        if (keyListener != null) {
            keyListener.keyReleased(e);
        }
    }
}
