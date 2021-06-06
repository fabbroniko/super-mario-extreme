package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.scene.GameScene;

/**
 * Represents the simplest block in the game.
 * It can be break if hit from the bottom.
 * @author com.fabbroniko
 */
public class Block extends AbstractGameObject implements AnimationListener {

	private static final Dimension spriteDimension = new Dimension(120, 120);
	private static final String spritePath = "/sprites/block.png";

	public static final String BLOCK_IDLE_ANIMATION_NAME = "BLOCK_IDLE";
	public static final String BLOCK_BREAKING_ANIMATION_NAME = "BLOCK_BREAKING";

	private final Animation breakingAnimation;

	public Block(final TileMap tileMap, final GameScene gameScene, final Position position) {
		super(tileMap, gameScene, position, spriteDimension);

		breakingAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(6)
				.frameDuration(50)
				.animationListener(this)
				.name(BLOCK_BREAKING_ANIMATION_NAME)
				.build();

		setAnimation(Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(BLOCK_IDLE_ANIMATION_NAME)
				.build());
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
