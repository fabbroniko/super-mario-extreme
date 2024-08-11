package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public interface Scene extends KeyListener {

    void init();

    void update();

    BufferedImage draw();

    default int getCenteredXPositionForString(final String text, final Graphics2D g, final Dimension2D dimension) {
        return (dimension.width() - g.getFontMetrics().stringWidth(text)) / 2;
    }

    default int getCenteredXPositionFromSize(final Dimension2D canvasDimension, final int secondaryWidth) {
        return (canvasDimension.width() - secondaryWidth) / 2;
    }

    void detach();

    boolean isClosed();
}
