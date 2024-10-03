package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;

public class FallingBlock extends AbstractGameObject {

	private static final Vector2D spriteDimension = new Vector2D(360, 120);
	private static final String spritePath = "/sprites/falling-block.png";

	public static final String FALLING_BLOCK_IDLE_ANIMATION_NAME = "FALL_BLK_IDLE";

	public FallingBlock(final TileMap tileMap,
						final GameScene gameScene,
						final ImageLoader imageLoader,
						final EffectPlayerProvider effectPlayerProvider,
						final Vector2D position) {
		super(tileMap, gameScene, imageLoader, effectPlayerProvider, position, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(FALLING_BLOCK_IDLE_ANIMATION_NAME)
				.build());
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject collidedGameObject) {
		super.handleObjectCollisions(direction, collidedGameObject);
		
		if (collidedGameObject instanceof Player && direction.equals(CollisionDirection.TOP_COLLISION)) {
			falling = true;
		}
	}
}
