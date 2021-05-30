package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * A tile is a fragment of the tilemap which includes the sub-images of all the possible terrains that can be drawn in
 * the game canvas.
 *
 * On game startup the tilemap is split into its individual tiles, representing different types of terrain like Dirt, Grass, Water, Pipes and so on.
 * Tiles are unique - this means that it should not be possible to have instances of this class with the same tile image and tile type.
 * In the future it could be possible to have the same tile with different properties (aka tile type). For example it could be possible
 * to create a trap from a tile that looks like grass but with {@link TileType#NON_BLOCKING NON_BLOCKING} physics.
 *
 * @see TileType
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Tile {

	private final BufferedImage image;
	private final TileType type;
}