package com.fabbroniko.scene;

import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public interface Scene extends KeyListener {

    void init();

    void update();

    BufferedImage draw();
}
