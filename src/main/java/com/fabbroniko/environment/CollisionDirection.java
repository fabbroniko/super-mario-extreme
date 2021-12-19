package com.fabbroniko.environment;

/**
 * Represents the direction of a collision.
 * @author com.fabbroniko
 */
public enum CollisionDirection {
	
	/**
	 * Collision from the top.
	 */
	TOP_COLLISION,
	
	/**
	 * Collision from the bottom.
	 */
	BOTTOM_COLLISION,
	
	/**
	 * Collision from the left side.
	 */
	LEFT_COLLISION,
	
	/**
	 * Collision from the right side.
	 */
	RIGHT_COLLISION;

	public static CollisionDirection invert(final CollisionDirection original) {
		if(TOP_COLLISION.equals(original))
			return BOTTOM_COLLISION;
		if(BOTTOM_COLLISION.equals(original))
			return TOP_COLLISION;
		if(RIGHT_COLLISION.equals(original))
			return LEFT_COLLISION;

		return RIGHT_COLLISION;
	}
}
