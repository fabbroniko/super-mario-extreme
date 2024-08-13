package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.AudioManager;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;
import com.fabbroniko.ui.DynamicDrawable;
import com.fabbroniko.main.Time;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;
import lombok.Getter;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameObject implements DynamicDrawable {

	protected Vector2D currentPosition;

	@Getter
	protected Vector2D spriteDimension;
	protected Vector2D mapPosition;
	protected Animation currentAnimation;
	protected List<Animation> registeredAnimations;
	protected final TileMap tileMap;
	protected final GameScene gameScene;
	protected final ResourceManager resourceManager;
	protected final AudioManager audioManager;

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
								 final ResourceManager resourceManager,
								 final AudioManager audioManager,
								 final Vector2D position,
								 final Vector2D spriteDimension) {
		this.tileMap = tileMap;
		this.gameScene = gameScene;
		this.resourceManager = resourceManager;
		this.audioManager = audioManager;
		this.death = false;
		
		this.currentPosition = position.clone();
		this.spriteDimension = spriteDimension;
		this.registeredAnimations = new ArrayList<>();

		offset = new Vector2D();
		mapPosition = new Vector2D();
	}

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

	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		handleMapCollisions(direction);
	}
	
	protected void setAnimation(final Animation animation) {
		animation.reset();
		this.currentAnimation = animation;
	}

	public final Rectangle getRectangle() {
		return new Rectangle(currentPosition.getRoundedX(), currentPosition.getRoundedY(), spriteDimension.getRoundedX(), spriteDimension.getRoundedY());
	}

	public boolean isDead() {
		return death;
	}
	
	public void notifyDeath() {
		this.death = true;
	}

	public Vector2D getPosition() {
		return currentPosition.clone();
	}

	public Vector2D getDimension() {
		return spriteDimension;
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
			currentPosition.setVector2D(currentPosition.getX() + offset.getX(), currentPosition.getY() + offset.getY());
		}
	}
	
	@Override
	public DrawableResource getDrawableResource() {
		final Position position = new ImmutablePosition(currentPosition.getX() - mapPosition.getX(), currentPosition.getY() - mapPosition.getY());
		if(facingRight) {
			return new DrawableResourceImpl(currentAnimation.getImage(), position);
		} else {
			return new DrawableResourceImpl(currentAnimation.getMirroredImage(), position);
		}
	}
}