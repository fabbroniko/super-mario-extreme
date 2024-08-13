package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;

import java.awt.image.BufferedImage;

public class SceneContextFactoryImpl implements SceneContextFactory {

    private final Dimension2D sceneDimension;

    public SceneContextFactoryImpl(final int width, final int height) {
        this.sceneDimension = new Dimension2D(width, height);
    }

    @Override
    public SceneContext create() {
        return new SceneContext(createImage(), sceneDimension);
    }

    private BufferedImage createImage() {
        return new BufferedImage(sceneDimension.width(), sceneDimension.height(), BufferedImage.TYPE_INT_RGB);
    }
}
