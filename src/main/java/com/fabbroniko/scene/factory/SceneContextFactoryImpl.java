package com.fabbroniko.scene.factory;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.scene.SceneContext;
import org.example.annotation.Component;

import java.awt.image.BufferedImage;

@Component
public class SceneContextFactoryImpl implements SceneContextFactory {

    private final Dimension2D canvasSize;

    public SceneContextFactoryImpl(final Dimension2D canvasSize) {
        this.canvasSize = canvasSize;
    }

    @Override
    public SceneContext create() {
        return new SceneContext(createImage(), canvasSize);
    }

    private BufferedImage createImage() {
        return new BufferedImage(canvasSize.width(), canvasSize.height(), BufferedImage.TYPE_INT_RGB);
    }
}
