package com.fabbroniko.resource;

import java.awt.image.BufferedImage;

public interface ImageLoader {

    BufferedImage findBackgroundByName(final String name);

    BufferedImage findTileMap();

    BufferedImage findSpritesByName(final String name);
}
