package com.fabbroniko.gameobjects;

import java.awt.Graphics2D;
import java.awt.Point;

import com.fabbroniko.environment.Animation;
import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.CollisionDirection;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.GameObjectBiDimensionalSpace;
import com.fabbroniko.environment.ObjectType;
import com.fabbroniko.environment.Service;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.gamestatemanager.AbstractGenericLevel;
import com.fabbroniko.main.Drawable;

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
	 * Object's type.
	 */
	protected ObjectType objectType;
	
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
	protected AbstractGenericLevel level;
	
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
	
	/**
	 * Represents the default offset for movements.
	 */
	protected int upOffset = -5;
	
	/**
	 * Represents the default offset for movements.
	 */
	protected int downOffset = 3;
	
	/**
	 * Represents the default offset for movements.
	 */
	protected int leftOffset = -3;
	
	/**
	 * Represents the default offset for movements.
	 */
	protected int rightOffset = 3;
	
	/**
	 * Represents the max jump.
	 */
	protected int maxJump = 20;
	
	// Collision rectangle
	/**
	 * Movement's offset.
	 */
	protected Position offset;
	
	/**
	 * Top Left Corner.
	 */
	protected Point topLeft;
	
	/**
	 * Top Right Corner.
	 */
	protected Point topRight;
	
	/**
	 * Bottom Left Corner.
	 */
	protected Point bottomLeft;
	
	/**
	 * Bottom Right Corner.
	 */
	protected Point bottomRight;
	
	private final int objectID;
	
	/**
	 * Constructs a new AbstractGameObject.
	 * @param tileMapP TileMap on which it has to be placed.
	 * @param levelP GenericLevel on which it has to be placed.
	 */
	protected AbstractGameObject(final TileMap tileMapP, final AbstractGenericLevel levelP, final Animations animation, final int objectID) {
		this.objectID = objectID;
		this.tileMap = tileMapP;
		this.level = levelP;
		this.death = false;
		this.objectType = animation.getObjectType();
		this.setAnimation(animation);
		
		this.gameObjectBiDimensionalSpace = new GameObjectBiDimensionalSpace(new Position(0, 0), objectType.getDimension());

		topLeft = new Point();
		topRight = new Point();
		bottomLeft = new Point();
		bottomRight = new Point();

		offset = new Position();
		mapPosition = Service.ORIGIN.clone();
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
	
	protected void setAnimation(final Animations animation) {
		this.currentAnimation = new Animation(animation);
	}
	
	/**
	 * Checks if the object is dead.
	 * @return Return true if it's dead, fale otherwise.
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
		return new Position(this.gameObjectBiDimensionalSpace.getPosition());
	}
	
	/**
	 * Sets the object's position.
	 * @param position The new object's position.
	 */
	public void setObjectPosition(final Position position) {
		this.gameObjectBiDimensionalSpace.setPosition(position.clone());
	}

	/**
	 * Gets the object's type.
	 * @return Returns the object's type.
	 */
	public ObjectType getObjectType() {
		return this.objectType;
	}
	
	@Override
	public boolean equals(final Object gameObject) {
		return gameObject instanceof AbstractGameObject && this.objectID == ((AbstractGameObject)gameObject).objectID;
	}
	
	@Override
	public int hashCode() {
		return this.getObjectPosition().hashCode();
	}
	
	@Override
	public void update() {
		int xOffset = 0;
		int yOffset = 0;
		
		mapPosition.copyPosition(tileMap.getPosition());
		
		if (jumping) {
			yOffset += upOffset;
			currentJump++;
			if (currentJump > maxJump) {
				jumping = false;
			}
		}
		
		yOffset += falling && !jumping ? downOffset : 0;
		xOffset += left ? leftOffset : 0;
		xOffset += right ? rightOffset : 0;
		
		if (xOffset != 0 || yOffset != 0) {
			offset.setX(xOffset);
			offset.setY(yOffset);
			level.checkForCollisions(this, offset);
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