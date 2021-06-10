package com.fabbroniko.gameobjects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fabbroniko.environment.*;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.Time;
import com.fabbroniko.scene.GameScene;

public abstract class AbstractGameObject implements Drawable {

	protected Vector2D currentPosition;
	protected Vector2D spriteDimension;
	protected Vector2D mapPosition;
	protected Animation currentAnimation;
	protected List<Animation> registeredAnimations;
	protected TileMap tileMap;
	protected GameScene gameScene;
	protected int currentJump;
	protected int jumpSpeed = -1000; // Pixels per second
	protected int gravitySpeed = 600; // Pixels per second
	protected int walkingSpeed = 600; // Pixels per second
	protected int maxJump = 400; // Pixels
	protected Vector2D offset;

	protected final Set<GameObjectProperty> properties;

	protected AbstractGameObject(final TileMap tileMapP, final GameScene gameScene, final Vector2D spawnPosition, final Vector2D spriteDimension) {
		this.tileMap = tileMapP;
		this.gameScene = gameScene;
		properties = Collections.synchronizedSet(new HashSet<>());
		
		this.currentPosition = spawnPosition.clone();
		this.spriteDimension = spriteDimension;
		this.registeredAnimations = new ArrayList<>();

		offset = new Vector2D();
		mapPosition = new Vector2D();
	}

	/**
	 * Handles collisions with the map.
	 * @param direction Collision's direction.
	 */
	public void handleMapCollisions(final CollisionDirection direction) {
		if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			properties.add(GameObjectProperty.GROUND_HIT);
			offset.setY(0);
		}
		if (direction.equals(CollisionDirection.TOP_COLLISION)) {
			properties.remove(GameObjectProperty.JUMP);
			offset.setY(0);
		}
		if (direction.equals(CollisionDirection.LEFT_COLLISION) || direction.equals(CollisionDirection.RIGHT_COLLISION)) {
			offset.setX(0);
		}
	}
	
	/**
	 * Handles collisions with other objects.
	 * @param direction Collision's Direction.
	 */
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
	
	/**
	 * Checks if the object is dead.
	 * @return Return true if it's dead, false otherwise.
	 */
	public boolean isDead() {
		return properties.contains(GameObjectProperty.DEAD);
	}
	
	public void notifyDeath() {
		properties.clear();
		properties.add(GameObjectProperty.DEAD);
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
		
		if (properties.contains(GameObjectProperty.JUMP)) {
			yOffset += (jumpSpeed * Time.deltaTime());
			currentJump += yOffset;
			if (currentJump < -maxJump) {
				properties.remove(GameObjectProperty.JUMP);
			}
		}
		
		yOffset += properties.contains(GameObjectProperty.FALLING) && !properties.contains(GameObjectProperty.JUMP) ? (gravitySpeed * Time.deltaTime()) : 0;
		xOffset += properties.contains(GameObjectProperty.MOVEMENT_LEFT) ? (-walkingSpeed * Time.deltaTime()) : 0;
		xOffset += properties.contains(GameObjectProperty.MOVEMENT_RIGHT) ? (walkingSpeed * Time.deltaTime()) : 0;
		
		if (xOffset != 0 || yOffset != 0) {
			offset.setX(xOffset);
			offset.setY(yOffset);
			gameScene.checkForCollisions(this, offset);
			currentPosition.setVector2D(currentPosition.getX() + offset.getX(), currentPosition.getY() + offset.getY());
		}
	}
	
	@Override
	public BufferedImage getDrawableImage() {
		if(properties.contains(GameObjectProperty.FACING_RIGHT)) {
			return currentAnimation.getImage();
		} else {
			return currentAnimation.getMirroredImage();
		}
	}

	@Override
	public Vector2D getDrawingPosition() {
		return new Vector2D(currentPosition.getX() - mapPosition.getX(), currentPosition.getY() - mapPosition.getY());
	}

	public Vector2D getSpriteDimension() {
		return spriteDimension;
	}
}