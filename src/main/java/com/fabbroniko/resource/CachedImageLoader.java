package com.fabbroniko.resource;

import org.example.annotation.Component;
import org.example.annotation.Qualifier;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Component
public class CachedImageLoader implements ImageLoader {

    private final ImageLoader imageLoader;
    private final Map<String, BufferedImage> cache = new HashMap<>();

    private BufferedImage tileMap;

    public CachedImageLoader(@Qualifier("diskImageLoader") final ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public BufferedImage findBackgroundByName(final String name) {
        return imageLoader.findBackgroundByName(name);
    }

    @Override
    public BufferedImage findTileMap() {
        if (this.tileMap == null) {
            this.tileMap = imageLoader.findTileMap();
        }

        return this.tileMap;
    }

    @Override
    public BufferedImage findSpritesByName(final String name) {
        if (!cache.containsKey(name)) {
            cache.put(name, imageLoader.findSpritesByName(name));
        }

        return cache.get(name);
    }
}
