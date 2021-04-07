package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.CollisionDirection;
import com.fabbroniko.environment.ObjectType;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.environment.Animation.AnimationListener;
import com.fabbroniko.gamestatemanager.AbstractGenericLevel;

/**
 * Represents an enemy that can only be killed if a player hits him from the top.
 * @author com.fabbroniko
 */
public class Enemy extends AbstractGameObject implements AnimationListener {
	
	private boolean init;
	
	/**
	 * Constructs a new Enemy.
	 * @param tileMap Reference of the {@link TileMap TileMap} on which it should be placed.
	 * @param level Reference of the {@link AbstractGenericLevel AbstractGenericLevel} on which it should be placed.
	 */
	public Enemy(final TileMap tileMap, final AbstractGenericLevel level, final Integer objectID) {
		super(tileMap, level, Animations.ENEMY_WALK, objectID); 
		falling = true;
		leftOffset = -1;
		rightOffset = 1;
	}
	
	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
		super.handleMapCollisions(direction);
		
		if (direction.equals(CollisionDirection.BOTTOM_COLLISION) && !init) {
			right = true;
			facingRight = true;
			init = true;
		}
		if (direction.equals(CollisionDirection.LEFT_COLLISION)) {
			left = false;
			facingRight = false;
			right = true;
		}
		if (direction.equals(CollisionDirection.RIGHT_COLLISION)) {
			right = false;
			facingRight = true;
			left = true;
		}
		
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (direction.equals(CollisionDirection.TOP_COLLISION) && obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && !currentAnimation.getAnimation().equals(Animations.ENEMY_DEAD)) {
			this.setAnimation(Animations.ENEMY_DEAD);
			this.currentAnimation.setAnimationListener(this);
			this.level.getAudioManager().playEffect("hit");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
