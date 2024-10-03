package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;

public class Enemy extends AbstractGameObject implements AnimationListener {

	private static final Vector2D spriteDimension = new Vector2D(108, 192);
	private static final String spritePath = "/sprites/ghost.png";
	private static final String ENEMY_WALK_ANIMATION_NAME = "ENEMY_WALK";
	private static final String ENEMY_DEAD_ANIMATION_NAME = "ENEMY_DEAD";
	private boolean init;
	private final Animation deadAnimation;

	public Enemy(final TileMap tileMap,
				 final GameScene gameScene,
				 final ImageLoader imageLoader,
				 final EffectPlayerProvider effectPlayerProvider,
				 final Vector2D position) {
		super(tileMap, gameScene, imageLoader, effectPlayerProvider, position, spriteDimension);

		falling = true;
		walkingSpeed = 300;

		deadAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.frameDuration(200)
				.animationListener(this)
				.name(ENEMY_DEAD_ANIMATION_NAME)
				.mirror()
				.build();

		setAnimation(Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(2)
				.frameDuration(100)
				.name(ENEMY_WALK_ANIMATION_NAME)
				.mirror()
				.build());
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
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject collidedGameObject) {
		super.handleObjectCollisions(direction, collidedGameObject);
		
		if (direction.equals(CollisionDirection.TOP_COLLISION) && collidedGameObject instanceof Player && !currentAnimation.getName().equals(ENEMY_DEAD_ANIMATION_NAME)) {
			this.setAnimation(deadAnimation);
			effectPlayerProvider.getEffectPlayer().play("hit");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
