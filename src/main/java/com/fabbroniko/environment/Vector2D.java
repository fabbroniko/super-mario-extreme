package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * This class represents a point in a 2D space (x, y coordinates).
 * Coordinates are stored as double to maintain high precision when calculating the new position of an object in the 2d
 * space using delta time elapsed between frames, but they can be retrieved as integer when accessed to draw an object in
 * a canvas (as the measurement unit is pixels).
 */
@Data
@AllArgsConstructor
public class Vector2D implements Cloneable {

	private double x;
	private double y;

	/**
	 * A new Vector2D instance with default values of 0, 0.
	 */
	public Vector2D() {
		this(0, 0);
	}

	/**
	 * Sets new coordinates for this instance using another instance's values.
	 * @param model The instance of this class to use to copy the coordinates.
	 */
	public void setVector2D(final Vector2D model) {
		this.setVector2D(model.getX(), model.getY());
	}

	/**
	 * Sets new coordinates for this instance
	 * @param x The X component of the new coordinate.
	 * @param y The Y component of the new coordinate.
	 */
	public void setVector2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the rounded value of X to the nearest integer.
	 * @return The rounded X value to the nearest integer.
	 */
	public int getRoundedX() {
		return (int)Math.round(x);
	}

	/**
	 * Returns the rounded value of Y to the nearest integer.
	 * @return The rounded Y value to the nearest integer.
	 */
	public int getRoundedY() {
		return (int)Math.round(y);
	}

	/**
	 * Returns a copy of this Vector2D object
	 * Allows the JVM to perform field-for-field copy of the instance of this class. If this class wasn't implementing
	 * the Cloneable interface the call to {@link Object#clone() Object#clone}  would fail.
	 *
	 * @see Cloneable
	 * @return A field-for-field copy of the instance of this class.
	 */
	@SneakyThrows
	@Override
	public Vector2D clone() {
		return (Vector2D) super.clone();
	}
}
