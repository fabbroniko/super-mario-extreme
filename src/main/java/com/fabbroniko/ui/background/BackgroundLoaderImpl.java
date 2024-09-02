package com.fabbroniko.ui.background;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.ui.InitializableDrawable;
import org.example.annotation.Component;
import org.example.annotation.Qualifier;

import java.awt.Color;

@Component
public class BackgroundLoaderImpl implements BackgroundLoader {

    private final ImageLoader imageLoader;
    private final DrawableResourceFactory drawableResourceFactory;

    public BackgroundLoaderImpl(@Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                                final DrawableResourceFactory drawableResourceFactory) {
        this.imageLoader = imageLoader;
        this.drawableResourceFactory = drawableResourceFactory;
    }

    @Override
    public InitializableDrawable createStaticBackground(final String resourceName) {
        return new StaticBackground(imageLoader.findBackgroundByName(resourceName), drawableResourceFactory);
    }

    @Override
    public InitializableDrawable createSimpleColorBackground(final Color color, final Dimension2D canvasDimension) {
        return new SimpleColorBackground(color, canvasDimension, drawableResourceFactory);
    }
}
