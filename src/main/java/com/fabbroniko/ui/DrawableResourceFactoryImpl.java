package com.fabbroniko.ui;

import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public class DrawableResourceFactoryImpl implements DrawableResourceFactory {

    @Override
    public DrawableResource createDrawableResource(final BufferedImage image) {
        return createDrawableResource(image, new ImmutablePosition(0, 0));
    }

    @Override
    public DrawableResource createDrawableResource(final BufferedImage image, final Position position) {
        return new DrawableResourceImpl(image, position);
    }
}
