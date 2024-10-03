package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;

public class Block extends AbstractGameObject implements AnimationListener {

	private static final Vector2D spriteDimension = new Vector2D(120, 120);
	private static final String spritePath = "/sprites/block.png";

	public static final String BLOCK_IDLE_ANIMATION_NAME = "BLOCK_IDLE";
	public static final String BLOCK_BREAKING_ANIMATION_NAME = "BLOCK_BREAKING";

	private final Animation breakingAnimation;

	public Block(final TileMap tileMap,
				 final GameScene gameScene,
				 final ImageLoader imageLoader,
				 final EffectPlayerProvider effectPlayerProvider,
				 final Vector2D position) {
		super(tileMap, gameScene, imageLoader, effectPlayerProvider, position, spriteDimension);

		breakingAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(6)
				.frameDuration(50)
				.animationListener(this)
				.name(BLOCK_BREAKING_ANIMATION_NAME)
				.build();

		setAnimation(Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
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
			effectPlayerProvider.getEffectPlayer().play("breaking");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
