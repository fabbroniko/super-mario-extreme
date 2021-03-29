package fabbroniko.gameobjects;

import fabbroniko.environment.Animations;
import fabbroniko.environment.TileMap;
import fabbroniko.gamestatemanager.AbstractGenericLevel;

/**
 * Represents the only way, for a player, to finish a level.
 * @author fabbroniko
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
