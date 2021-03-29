package fabbroniko.gameobjects;

import fabbroniko.environment.Animations;
import fabbroniko.environment.AudioManager;
import fabbroniko.environment.CollisionDirection;
import fabbroniko.environment.ObjectType;
import fabbroniko.environment.TileMap;
import fabbroniko.environment.Animation.AnimationListener;
import fabbroniko.gamestatemanager.AbstractGenericLevel;
import fabbroniko.resources.Sound;

/**
 * Represents the simplest block in the game.
 * It can be break if hit from the bottom.
 * @author fabbroniko
 */
public class Block extends AbstractGameObject implements AnimationListener {
	
	/**
	 * Constructs a new Block.
	 * @param tileMap Reference of the {@link TileMap TileMap} on which it should be placed.
	 * @param level Reference of the {@link AbstractGenericLevel AbstractGenericLevel} on which it should be placed.
	 */
	public Block(final TileMap tileMap, final AbstractGenericLevel level, final Integer objectID) {
		super(tileMap, level, Animations.BLOCK_NORMAL, objectID);
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && direction.equals(CollisionDirection.BOTTOM_COLLISION) && !currentAnimation.equals(Animations.BLOCK_BREAKING)) {
			this.setAnimation(Animations.BLOCK_BREAKING);
			currentAnimation.setAnimationListener(this);
			AudioManager.getInstance().playSound(Sound.getSoundFromName("BreakingBlockSound"));
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
