package com.fabbroniko.main;

import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public interface DrawableResourceFactory {

    DrawableResource createDrawableResource(final BufferedImage image, final Position position);
}
