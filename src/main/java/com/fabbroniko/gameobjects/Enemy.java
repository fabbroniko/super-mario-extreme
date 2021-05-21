package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.environment.Animation.AnimationListener;
import com.fabbroniko.scene.GameScene;

import java.awt.image.BufferedImage;
import java.util.List;

public class Enemy extends AbstractGameObject implements AnimationListener {

	private static final Dimension spriteDimension = new Dimension(27, 48);
	private static final String spritePath = "/sprites/ghost.png";
	private static final String ENEMY_WALK_ANIMATION_NAME = "ENEMY_WALK";
	private static final String ENEMY_DEAD_ANIMATION_NAME = "ENEMY_DEAD";
	private boolean init;
	private final Animation deadAnimation;

	public Enemy(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, objectID, spriteDimension);
		falling = true;
		leftOffset = -1;
		rightOffset = 1;

		List<BufferedImage> frames = generateSprites(spritePath, spriteDimension, 1, 1);
		deadAnimation = new Animation(ENEMY_DEAD_ANIMATION_NAME, frames, 7, true, this);

		frames = generateSprites(spritePath, spriteDimension, 0, 2);
		final Animation walkAnimation = new Animation(ENEMY_WALK_ANIMATION_NAME, frames, 5, false, null);
		setAnimation(walkAnimation);
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
		
		if (direction.equals(CollisionDirection.TOP_COLLISION) && obj instanceof Player && !currentAnimation.getName().equals(ENEMY_DEAD_ANIMATION_NAME)) {
			this.setAnimation(deadAnimation);
			this.gameScene.getAudioManager().playEffect("hit");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
