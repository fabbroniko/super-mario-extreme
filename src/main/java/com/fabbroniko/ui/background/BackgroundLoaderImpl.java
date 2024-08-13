package com.fabbroniko.ui.background;

import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.ui.Drawable;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.resource.ResourceManager;

public class BackgroundLoaderImpl implements BackgroundLoader {

    private final ResourceManager resourceManager;
    private final DrawableResourceFactory drawableResourceFactory;

    public BackgroundLoaderImpl(final ResourceManager resourceManager,
                                final DrawableResourceFactory drawableResourceFactory) {
        this.resourceManager = resourceManager;
        this.drawableResourceFactory = drawableResourceFactory;
    }

    @Override
    public Drawable createStaticBackground(String resourceName) {
        return new StaticBackground(resourceManager.findBackgroundFromName(resourceName), new ImmutablePosition(0, 0), drawableResourceFactory);
    }
}
