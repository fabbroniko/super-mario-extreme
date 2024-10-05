package com.fabbroniko.map;

import com.fabbroniko.environment.LevelProvider;
import com.fabbroniko.resource.dto.MapDto;
import com.fabbroniko.resource.dto.TileDto;
import com.fabbroniko.sdi.annotation.Component;

@Component
public class DefaultMapLoader implements MapLoader {

    private static final int NO_TILE = -1;

    private final LevelProvider levelProvider;

    public DefaultMapLoader(final LevelProvider levelProvider) {
        this.levelProvider = levelProvider;
    }

    @Override
    public int[][] load() {
        final MapDto map = levelProvider.getLevel().getMap();
        final int nRows = map.getVerticalBlocks();
        final int nCols = map.getHorizontalBlocks();

        int[][] mapMatrix = new int[nRows][nCols];
        for(int i = 0; i < nRows; i++) {
            for(int y = 0; y < nCols; y++) {
                mapMatrix[i][y] = NO_TILE;
            }
        }

        for(final TileDto t : map.getTileDtos()) {
            mapMatrix[t.getVerticalIndex()][t.getHorizontalIndex()] = t.getId();
        }

        return mapMatrix;
    }
}
