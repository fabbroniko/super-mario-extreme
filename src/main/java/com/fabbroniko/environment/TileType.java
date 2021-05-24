package com.fabbroniko.environment;

import com.fabbroniko.error.TileTypeError;

public enum TileType {

    /**
     * Represents a non-blocking tile.
     */
    TILE_FREE(0),

    /**
     * Represents a blocking tile.
     */
    TILE_BLOCK(1);

    private final int tileType;

    TileType(final int i) {
        this.tileType = i;
    }

    private int getType() {
        return this.tileType;
    }

    /**
     * Gets the TileType associated with the given index.
     * @param index Value associated with each TileType.
     * @return Returns the corresponding TileType.
     */
    public static TileType getTileType(final int index) {
        for (final TileType i: TileType.values()) {
            if (i.getType() == index) {
                return i;
            }
        }

        throw new TileTypeError(index);
    }
}
