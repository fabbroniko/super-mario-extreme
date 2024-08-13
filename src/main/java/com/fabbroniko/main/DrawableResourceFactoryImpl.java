package com.fabbroniko.main;

import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public class DrawableResourceFactoryImpl implements DrawableResourceFactory {

    @Override
    public DrawableResource createDrawableResource(final BufferedImage image, final Position position) {
        return new DrawableResourceImpl(image, position);
    }
}
