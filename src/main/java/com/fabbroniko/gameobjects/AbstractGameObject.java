package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameObject implements GameObject {

	protected BoundingBox boundingBox;

	protected Vector2D mapPosition;
	protected Animation currentAnimation;
	protected List<Animation> registeredAnimations;
	protected final TileMap tileMap;
	protected final GameScene gameScene;
	protected final ImageLoader imageLoader;
	protected final EffectPlayerProvider effectPlayerProvider;

	protected boolean jumping;
	protected boolean falling;
	protected boolean left;
	protected boolean right;
	protected boolean facingRight;
	protected boolean groundHit;
	protected int currentJump;
	protected boolean death;
	protected int jumpSpeed = -1000;
	protected int gravitySpeed = 600;
	protected int walkingSpeed = 600;
	protected int maxJump = 400;

	protected Vector2D offset;

	protected AbstractGameObject(final TileMap tileMap,
								 final GameScene gameScene,
								 final ImageLoader imageLoader,
								 final EffectPlayerProvider effectPlayerProvider,
								 final Vector2D position,
								 final Dimension2D dimension) {
		this.tileMap = tileMap;
		this.gameScene = gameScene;
		this.imageLoader = imageLoader;
		this.effectPlayerProvider = effectPlayerProvider;
		this.death = false;
		
		this.boundingBox = new BoundingBox(position, dimension);
		this.registeredAnimations = new ArrayList<>();

		offset = new Vector2D();
		mapPosition = new Vector2D();
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
	
	protected void setAnimation(final Animation animation) {
		animation.reset();
		this.currentAnimation = animation;
	}

	public boolean isDead() {
		return death;
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
}