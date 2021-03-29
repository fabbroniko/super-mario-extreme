package fabbroniko.gameobjects;

import fabbroniko.environment.Animations;
import fabbroniko.environment.AudioManager;
import fabbroniko.environment.CollisionDirection;
import fabbroniko.environment.ObjectType;
import fabbroniko.environment.TileMap;
import fabbroniko.gamestatemanager.AbstractGenericLevel;
import fabbroniko.resources.Sound;

/**
 * Represents an invisible block. If visible, it follows the same rules of a normal block. Otherwise it can only be hit from below.
 * @author fabbroniko
 */
public class InvisibleBlock extends AbstractGameObject {
	
	/**
	 * Constructs a new InvisibleBlock.
	 * @param tileMap Reference of the {@link TileMap TileMap} on which it should be placed.
	 * @param level Reference of the {@link AbstractGenericLevel AbstractGenericLevel} on which it should be placed.
	 */
	public InvisibleBlock(final TileMap tileMap, final AbstractGenericLevel level, final Integer objectID) {
		super(tileMap, level, Animations.INVISIBLEBLOCK_INVISIBLE, objectID);
		this.objectType = ObjectType.TYPE_INVISIBLE_BLOCK;
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) 
	{
		if (obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			this.setAnimation(Animations.INVISIBLEBLOCK_VISIBLE);
			AudioManager.getInstance().playSound(Sound.getSoundFromName("HitSound"));
		}
	}
}
