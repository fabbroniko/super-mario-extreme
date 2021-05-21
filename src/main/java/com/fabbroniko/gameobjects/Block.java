package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.environment.Animation.AnimationListener;
import com.fabbroniko.scene.GameScene;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Represents the simplest block in the game.
 * It can be break if hit from the bottom.
 * @author com.fabbroniko
 */
public class Block extends AbstractGameObject implements AnimationListener {

	private static final Dimension spriteDimension = new Dimension(30, 30);
	private static final String spritePath = "/sprites/block.png";

	public static final String BLOCK_IDLE_ANIMATION_NAME = "BLOCK_IDLE";
	public static final String BLOCK_BREAKING_ANIMATION_NAME = "BLOCK_BREAKING";

	private final Animation breakingAnimation;

	public Block(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, objectID, spriteDimension);

		List<BufferedImage> frames = generateSprites(spritePath, spriteDimension, 1, 6);
		breakingAnimation = new Animation(BLOCK_BREAKING_ANIMATION_NAME, frames, 4, true, this);

		frames = generateSprites(spritePath, spriteDimension, 0, 1);
		final Animation idleAnimation = new Animation(BLOCK_IDLE_ANIMATION_NAME, frames, 1, true, null);
		setAnimation(idleAnimation);
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (obj instanceof Player && direction.equals(CollisionDirection.BOTTOM_COLLISION) && !currentAnimation.getName().equals(BLOCK_BREAKING_ANIMATION_NAME)) {
			this.setAnimation(breakingAnimation);
			gameScene.getAudioManager().playEffect("breaking");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
