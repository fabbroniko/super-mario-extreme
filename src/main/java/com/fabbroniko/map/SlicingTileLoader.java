package com.fabbroniko.map;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlicingTileLoader implements TileLoader {

    private static final int LAST_SOLID_TILE_INDEX = 6;

    private final Dimension2D tileSize = new ImmutableDimension2D(120, 120);
    private final ImageLoader imageLoader;


    public SlicingTileLoader(@Qualifier("cachedImageLoader") final ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public List<Tile> load() {
        final BufferedImage tileSet = imageLoader.findTileMap();
        final List<Tile> tiles = new ArrayList<>();

        for (int currentX = 0; currentX < tileSet.getWidth(); currentX += tileSize.width()) {
            TileType tt = TileType.BLOCKING;
            if(currentX/tileSize.width() > LAST_SOLID_TILE_INDEX)
                tt = TileType.NON_BLOCKING;

            tiles.add(new Tile(tileSet.getSubimage(currentX, 0, tileSize.width(), tileSize.height()), tt));
        }

        return tiles;
    }
}
