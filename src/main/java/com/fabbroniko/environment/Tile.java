package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * The tile map is what we can consider as the map layout of the game. This includes static elements like terrain, pipes and similar.
 * The map is created by drawing tiles one next to the other on the game canvas and tiles can have different properties like
 * allowing game objects to move freely through them (like grass) or being blocked by them (like terrain).
 *
 * By knowing the layout of the map (where tiles are located) and the position of the player relative to the map, we can easily draw
 * the tile map on the game canvas and decide whether a game object is allowed to move to a certain position or not.
 * @see TileType
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Tile {

	/*
	 * The BufferedImage representing the tile.
	 * This is a single element like a patch of grass, or a block of dirt or the base of a pipe.
	 */
	private final BufferedImage image;

	/*
	 * The tile's properties
	 */
	private final TileType type;
}