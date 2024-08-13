package com.fabbroniko.ui.background;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.ui.InitializableDrawable;

import java.awt.Color;

public class BackgroundLoaderImpl implements BackgroundLoader {

    private final ResourceManager resourceManager;
    private final DrawableResourceFactory drawableResourceFactory;

    public BackgroundLoaderImpl(final ResourceManager resourceManager,
                                final DrawableResourceFactory drawableResourceFactory) {
        this.resourceManager = resourceManager;
        this.drawableResourceFactory = drawableResourceFactory;
    }

    @Override
    public InitializableDrawable createStaticBackground(final String resourceName) {
        return new StaticBackground(resourceManager.findBackgroundFromName(resourceName), drawableResourceFactory);
    }

    @Override
    public InitializableDrawable createSimpleColorBackground(final Color color, final Dimension2D canvasDimension) {
        return new SimpleColorBackground(color, canvasDimension, drawableResourceFactory);
    }
}
