package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Scene {

    void init();

    void update();

    BufferedImage draw();

    default int getCenteredXPositionForString(final String text, final Graphics2D g, final Dimension2D dimension) {
        return (dimension.getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
    }

    default int getCenteredXPositionFromSize(final Dimension2D canvasDimension, final int secondaryWidth) {
        return (canvasDimension.getWidth() - secondaryWidth) / 2;
    }

    void detach();
}
