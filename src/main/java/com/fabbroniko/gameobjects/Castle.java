package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

public class Castle extends AbstractGameObject {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(340, 350);
	private static final String spritePath = "/sprites/castle.png";

	public static final String CASTLE_IDLE_ANIMATION_NAME = "CASTLE_IDLE";

	public Castle(final TileMap tileMap,
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
				.name(CASTLE_IDLE_ANIMATION_NAME)
				.build());
	}

	@Override
	public void update() {
		double xOffset = 0;
		double yOffset = 0;

		mapPosition.setVector2D(tileMap.getPosition());

		if (jumping) {
			yOffset += (jumpSpeed * Time.deltaTime());
			currentJump += yOffset;
			if (currentJump < -maxJump) {
				jumping = false;
			}
		}

		yOffset += falling && !jumping ? (gravitySpeed * Time.deltaTime()) : 0;
		xOffset += left ? (-walkingSpeed * Time.deltaTime()) : 0;
		xOffset += right ? (walkingSpeed * Time.deltaTime()) : 0;

		if (xOffset != 0 || yOffset != 0) {
			offset.setX(xOffset);
			offset.setY(yOffset);
			gameScene.checkForCollisions(this, offset);
			boundingBox.position().setVector2D(boundingBox.position().getX() + offset.getX(), boundingBox.position().getY() + offset.getY());
		}
	}

	@Override
	public DrawableResource getDrawableResource() {
		final Position position = new ImmutablePosition(boundingBox.position().getRoundedX() - mapPosition.getX(), boundingBox.position().getRoundedY() - mapPosition.getY());
		if(facingRight) {
			return new DrawableResourceImpl(currentAnimation.getImage(), position);
		} else {
			return new DrawableResourceImpl(currentAnimation.getMirroredImage(), position);
		}
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
		if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			groundHit = true;
			offset.setY(0);
		}
		if (direction.equals(CollisionDirection.TOP_COLLISION)) {
			jumping = false;
			offset.setY(0);
		}
		if (direction.equals(CollisionDirection.LEFT_COLLISION) || direction.equals(CollisionDirection.RIGHT_COLLISION)) {
			offset.setX(0);
		}
	}

	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject gameObject) {
		handleMapCollisions(direction);
	}
}
