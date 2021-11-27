package com.fabbroniko.gameobjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	protected final GameScene gameScene;
	protected HashSet<State> currentStates;

	private final Vector2D cachedMapSize;

	protected int jumpSpeed = 1000; // Pixels per second
	protected int gravitySpeed = 600; // Pixels per second
	protected int walkingSpeed = 600; // Pixels per second
	//protected int maxJump = 400; // Pixels

	protected AbstractGameObject(final TileMap tileMapP, final GameScene gameScene, final Vector2D spawnPosition, final Vector2D spriteDimension) {
		this.tileMap = tileMapP;
		this.gameScene = gameScene;
		
		this.currentPosition = spawnPosition.clone();
		this.spriteDimension = spriteDimension;
		this.registeredAnimations = new ArrayList<>();
		this.currentStates = new HashSet<>();

		mapPosition = new Vector2D();
		cachedMapSize = tileMapP.getMapSize();
	}
	
	protected void setAnimation(final Animation animation) {
		if(currentAnimation != null && animation.getName().equals(currentAnimation.getName()))
			return;

		animation.reset();
		this.currentAnimation = animation;
	}
	
	/**
	 * Checks if the object is dead.
	 * @return Return true if it's dead, false otherwise.
	 */
	public boolean isDead() {
		return currentStates.contains(State.DEAD);
	}

	public Vector2D getPosition() {
		return currentPosition.clone();
	}

	public Vector2D getDimension() {
		return spriteDimension;
	}

	@Override
	public void update() {
		Vector2D offset = new Vector2D();

		mapPosition.setVector2D(tileMap.getPosition());
		
		if(currentStates.contains(State.MOVING_UP))
			offset.sum(0, -(jumpSpeed * Time.deltaTime()));

		if(currentStates.contains(State.MOVING_DOWN))
			offset.sum(0, gravitySpeed * Time.deltaTime());

		if(currentStates.contains(State.MOVING_LEFT))
			offset.sum(-walkingSpeed * Time.deltaTime(), 0);

		if(currentStates.contains(State.MOVING_RIGHT))
			offset.sum(walkingSpeed * Time.deltaTime(), 0);

		if((offset.getX() < 0) && currentStates.contains(State.FACING_RIGHT))
			currentStates.remove(State.FACING_RIGHT);
		else if (offset.getX() > 0)
			currentStates.add(State.FACING_RIGHT);

		if (offset.getX() != 0 || offset.getY() != 0) {
			final CollisionManager.CollisionResult collisionResult = gameScene.getCollisionManager().detectCollisions(this, offset);
			offset = collisionResult.getOffset();
			currentPosition.setVector2D(currentPosition.getX() + offset.getX(), currentPosition.getY() + offset.getY());

			movementDirection(offset.getX() != 0, offset.getY() != 0);

			// Check if the new offset positions the game object outside the boundaries of the map.
			// If it does, kill the game object.
			if(((currentPosition.getX() + spriteDimension.getRoundedX() - 1) < 0) ||
					((currentPosition.getY() + spriteDimension.getRoundedY() - 1) < 0) ||
					(currentPosition.getX() >= cachedMapSize.getRoundedX()) ||
					(currentPosition.getY() >= cachedMapSize.getRoundedY())
			) {
				currentStates.clear();
				currentStates.add(State.DEAD);
			}
		} else {
			movementDirection(false, false);
		}
	}

	protected abstract void movementDirection(final boolean horizontal, final boolean vertical);
	
	@Override
	public BufferedImage getDrawableImage() {
		if(currentStates.contains(State.FACING_RIGHT)) {
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

	public enum State {

		FACING_RIGHT,

		MOVING_LEFT,

		MOVING_RIGHT,

		MOVING_UP,

		MOVING_DOWN,

		DEAD
	}
}