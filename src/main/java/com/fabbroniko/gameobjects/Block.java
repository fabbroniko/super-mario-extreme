package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

public class Block implements AnimationListener, GameObject {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(120, 120);
	private static final String spritePath = "/sprites/block.png";

	public static final String BLOCK_IDLE_ANIMATION_NAME = "BLOCK_IDLE";
	public static final String BLOCK_BREAKING_ANIMATION_NAME = "BLOCK_BREAKING";

	private final Animation breakingAnimation;
	private final BoundingBox boundingBox;

	private Vector2D mapPosition = new Vector2D();
	private Animation currentAnimation;
	private final TileMap tileMap;
	private final EffectPlayerProvider effectPlayerProvider;

	private boolean death = false;

	public Block(final TileMap tileMap,
				 final ImageLoader imageLoader,
				 final EffectPlayerProvider effectPlayerProvider,
				 final Vector2D position) {

		this.tileMap = tileMap;
		this.effectPlayerProvider = effectPlayerProvider;
		this.boundingBox = new BoundingBox(position, spriteDimension);

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
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject collidedGameObject) {
		handleMapCollisions(direction);
		
		if (collidedGameObject instanceof Player && direction.equals(CollisionDirection.BOTTOM_COLLISION) && !currentAnimation.getName().equals(BLOCK_BREAKING_ANIMATION_NAME)) {
			this.setAnimation(breakingAnimation);
			effectPlayerProvider.getEffectPlayer().play("breaking");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}

	@Override
	public void update() {
		mapPosition.setVector2D(tileMap.getPosition());
	}

	@Override
	public DrawableResource getDrawableResource() {
		final Position position = new ImmutablePosition(boundingBox.position().getRoundedX() - mapPosition.getX(), boundingBox.position().getRoundedY() - mapPosition.getY());
		return new DrawableResourceImpl(currentAnimation.getImage(), position);
	}

	@Override
	public void notifyDeath() {
		this.death = true;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
	}

	@Override
	public boolean isDead() {
		return death;
	}

	private void setAnimation(final Animation animation) {
		animation.reset();
		this.currentAnimation = animation;
	}
}
