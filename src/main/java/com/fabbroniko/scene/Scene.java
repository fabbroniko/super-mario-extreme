package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;

import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public interface Scene extends KeyListener {

    void init();

    void update();

    BufferedImage draw();

    default int getCenteredXPosition(final BufferedImage image, final Dimension2D dimension) {
        return (dimension.width() - image.getWidth()) / 2;
    }
}
