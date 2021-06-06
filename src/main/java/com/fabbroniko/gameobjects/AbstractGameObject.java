package com.fabbroniko.gameobjects;

import java.awt.Graphics2D;

import com.fabbroniko.environment.*;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.Time;
import com.fabbroniko.scene.GameScene;

/**
 * Abstract Class representing a generic GameObject.
 * @author com.fabbroniko
 */
public abstract class AbstractGameObject implements Drawable {

	protected GameObjectBiDimensionalSpace gameObjectBiDimensionalSpace;
	/**
	 * Map's Position.
	 */
	protected Position mapPosition;
	
	/**
	 * Object's current animation.
	 */
	protected Animation currentAnimation;
	
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
	
	/**
	 * Represents whether it's facing right or not.
	 */
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
		
		this.gameObjectBiDimensionalSpace = new GameObjectBiDimensionalSpace(spawnPosition, spriteDimension);

		offset = new Position();
		mapPosition = new Position();
	}
		
	public GameObjectBiDimensionalSpace getBiDimensionalSpace() {
		return this.gameObjectBiDimensionalSpace;
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
	
	/**
	 * Gets the object's position.
	 * @return The Object's position.
	 */
	public Position getObjectPosition() {
		return gameObjectBiDimensionalSpace.getPosition().clone();
	}
	
	@Override
	public void update() {
		int xOffset = 0;
		int yOffset = 0;

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
			gameObjectBiDimensionalSpace.movePosition(offset);
		}
	}
	
	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		Position myPosition = this.gameObjectBiDimensionalSpace.getPosition();
		Dimension spriteDimension = this.gameObjectBiDimensionalSpace.getDimension();
		if (facingRight) {
			g.drawImage(currentAnimation.getImage(), myPosition.getX() - mapPosition.getX(), myPosition.getY() - mapPosition.getY(), spriteDimension.getWidth(), spriteDimension.getHeight(), null);
		} else {
			g.drawImage(currentAnimation.getImage(), myPosition.getX() - mapPosition.getX() + spriteDimension.getWidth(), myPosition.getY() - mapPosition.getY(),  -spriteDimension.getWidth(), spriteDimension.getHeight(), null);
		}
	}
}