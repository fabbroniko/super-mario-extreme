package com.fabbroniko.gameobjects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.fabbroniko.environment.*;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.Time;
import com.fabbroniko.scene.GameScene;

/**
 * Abstract Class representing a generic GameObject.
 * @author com.fabbroniko
 */
public abstract class AbstractGameObject implements Drawable {

	protected Position currentPosition;

	protected Dimension spriteDimension;
	/**
	 * Map's Position.
	 */
	protected Position mapPosition;
	
	/**
	 * Object's current animation.
	 */
	protected Animation currentAnimation;

	protected List<Animation> registeredAnimations;
	
	/**
	 * TileMap on which it has to be placed.
	 */
	protected TileMap tileMap;
	
	/**
	 * Level on which it has to be placed.
	 */
	protected GameScene gameScene;
	
	/**
	 * Represents whether it's jumping or not.
	 */
	protected boolean jumping;
	
	/**
	 * Represents whether it's falling or not.
	 */
	protected boolean falling;
	
	/**
	 * Represents it's going in the left direction.
	 */
	protected boolean left; 
	
	/**
	 * Represents it's going in the right direction.
	 */
	protected boolean right;

	protected boolean facingRight;
	
	/**
	 * Represents whether it hit the ground or not.
	 */
	protected boolean groundHit;
	
	/**
	 * Represents the current jumping height.
	 */
	protected int currentJump;
	
	/**
	 * Represents whether it's dead or not.
	 */
	protected boolean death;

	protected int jumpSpeed = -1000; // Pixels per second
	protected int gravitySpeed = 600; // Pixels per second
	protected int walkingSpeed = 600; // Pixels per second
	protected int maxJump = 400; // Pixels
	
	// Collision rectangle
	/**
	 * Movement's offset.
	 */
	protected Position offset;

	protected AbstractGameObject(final TileMap tileMapP, final GameScene gameScene, final Position spawnPosition, final Dimension spriteDimension) {
		this.tileMap = tileMapP;
		this.gameScene = gameScene;
		this.death = false;
		
		this.currentPosition = spawnPosition.clone();
		this.spriteDimension = spriteDimension;
		this.registeredAnimations = new ArrayList<>();

		offset = new Position();
		mapPosition = new Position();
	}

	/**
	 * Handles collisions with the map.
	 * @param direction Collision's direction.
	 */
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
		return new Rectangle(currentPosition.getRoundedX(), currentPosition.getRoundedY(), spriteDimension.getWidth(), spriteDimension.getHeight());
	}
	
	/**
	 * Checks if the object is dead.
	 * @return Return true if it's dead, false otherwise.
	 */
	public boolean isDead() {
		return death;
	}
	
	public void notifyDeath() {
		this.death = true;
	}

	public Position getPosition() {
		return currentPosition.clone();
	}

	public Dimension getDimension() {
		return spriteDimension;
	}

	@Override
	public void update() {
		double xOffset = 0;
		double yOffset = 0;

		mapPosition.setPosition(tileMap.getPosition());
		
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
			currentPosition.setPosition(currentPosition.getX() + offset.getX(), currentPosition.getY() + offset.getY());
		}
	}
	
	@Override
	public BufferedImage getDrawableImage() {
		if(facingRight) {
			return currentAnimation.getImage();
		} else {
			return currentAnimation.getMirroredImage();
		}
	}

	@Override
	public Position getDrawingPosition() {
		return new Position(currentPosition.getX() - mapPosition.getX(), currentPosition.getY() - mapPosition.getY());
	}

	public Dimension getSpriteDimension() {
		return spriteDimension;
	}
}