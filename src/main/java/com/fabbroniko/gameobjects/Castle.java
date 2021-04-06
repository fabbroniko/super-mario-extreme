package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.gamestatemanager.AbstractGenericLevel;

/**
 * Represents the only way, for a player, to finish a level.
 * @author com.fabbroniko
 *
 */
public class Castle extends AbstractGameObject {

	/**
	 * Constructs a new Castle.
	 * @param tileMap Reference of the {@link TileMap TileMap} on which it should be placed.
	 * @param level Reference of the {@link AbstractGenericLevel AbstractGenericLevel} on which it should be placed.
	 */
	public Castle(final TileMap tileMap, final AbstractGenericLevel level, final Integer objectID) {
		super(tileMap, level, Animations.CASTLE, objectID);
	}
}
