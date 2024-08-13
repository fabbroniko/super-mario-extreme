package com.fabbroniko.ui;

import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public interface DrawableResourceFactory {

    DrawableResource createDrawableResource(final BufferedImage image, final Position position);
}
