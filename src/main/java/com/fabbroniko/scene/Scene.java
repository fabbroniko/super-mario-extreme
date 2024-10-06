package com.fabbroniko.scene;

import java.awt.image.BufferedImage;

public interface Scene {

    void init();

    void update();

    BufferedImage draw();

    void close();
}
