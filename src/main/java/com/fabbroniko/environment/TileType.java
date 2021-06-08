package com.fabbroniko.environment;

/**
 * Describes a {@link Tile Tile's} properties.
 */
public enum TileType {

    /**
     * {@link com.fabbroniko.gameobjects.AbstractGameObject GameObjects} can move freely through this {@link Tile Tile}.
     */
    NON_BLOCKING,

    /**
     * {@link com.fabbroniko.gameobjects.AbstractGameObject GameObjects} collide with this {@link Tile Tile}, therefore
     * game objects can't go through this tile.
     */
    BLOCKING
}
