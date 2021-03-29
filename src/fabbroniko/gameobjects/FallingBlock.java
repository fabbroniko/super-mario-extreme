package fabbroniko.gameobjects;

import fabbroniko.environment.Animations;
import fabbroniko.environment.CollisionDirection;
import fabbroniko.environment.ObjectType;
import fabbroniko.environment.TileMap;
import fabbroniko.gamestatemanager.AbstractGenericLevel;

/**
 * A sequence of blocks that start falling when a player hit them from the top.
 * @author fabbroniko
 */
public class FallingBlock extends AbstractGameObject {
	
	
	/**
	 * Constructs a new FallingBlock.
	 * @param tileMap Reference of the {@link TileMap TileMap} on which it should be placed.
	 * @param level Reference of the {@link AbstractGenericLevel AbstractGenericLevel} on which it should be placed.
	 */
	public FallingBlock(final TileMap tileMap, final AbstractGenericLevel level, final Integer objectID) {
		super(tileMap, level, Animations.FALLING_BLOCK, objectID);
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && direction.equals(CollisionDirection.TOP_COLLISION)) {
			falling = true;
		}
	}
}
