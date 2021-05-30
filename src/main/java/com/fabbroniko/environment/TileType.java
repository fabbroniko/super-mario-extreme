package com.fabbroniko.environment;

/**
 * Describes what kind of physics is applied to a {@link Tile Tile}
 */
public enum TileType {

    /**
     * {@link com.fabbroniko.resource.domain.GameObject GameObjects} can move freely through this type of tile.
     */
    NON_BLOCKING,

    /**
     * {@link com.fabbroniko.resource.domain.GameObject GameObjects} collide with this type of tile and can't go through it, effectively behaving like a solid surface.
     */
    BLOCKING
}
