package fabbroniko.environment;

import java.awt.Point;

/**
 * Represents a position on the screen.
 * @author fabbroniko
 *
 */
public class Position implements Cloneable {

	private int x;
	private int y;
	
	private static final int HASH_CODE_MOLTIPLY_BY = 10000;
	
	public Position() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new position.
	 * @param xP X Coordinate.
	 * @param yP Y Coordinate.
	 */
	public Position(final int xP, final int yP) {
		this.x = xP;
		this.y = yP;
	}
	
	/**
	 * Constructs a new position, copying the one passed as parameter.
	 * @param cpy Position from which it will be copied.
	 */
	public Position(final Position cpy) {
		this(cpy.getX(), cpy.getY());
	}
	
	/**
	 * Constructs a new position starting from the {@link Point Point} passed as parameter.
	 * @param point Point from which it will be copied.
	 */
	public Position(final Point point) {
		this((int)point.getX(), (int)point.getY());
	}
	
	/**
	 * Change the current position.
	 * @param xP X Coordinate.
	 * @param yP Y Coordinate.
	 */
	public void setPosition(final int xP, final int yP) {
		this.x = xP;
		this.y = yP;
	}
	
	/**
	 * Copies the specified {@link Position Position}.
	 * @param pos Position from which it will be copied.
	 */
	public void copyPosition(final Position pos) {
		this.x = pos.getX();
		this.y = pos.getY();
	}
	
	/**
	 * X Getter.
	 * @return returns the x value of the actual position
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Y Getter.
	 * @return returns the y value of the actual position
	 */
	public int getY() {
		return this.y; 
	}
	
	/**
	 * Updates the x coord of the position.
	 * @param xP New X Position
	 */
	public void setX(final int xP) {
		this.x = xP;
	}
	
	/**
	 * Updates the y coord of the position.
	 * @param yP New Y Position.
	 */
	public void setY(final int yP) {
		this.y = yP;
	}
	
	@Override
	public String toString() {
		return "[X = " + this.x + ", Y = " + this.y + "]";
	}
	
	@Override
	public Position clone() {
		return new Position(this);
	}
	
	@Override
	public boolean equals(final Object pos) {
		return pos instanceof Position && this.x == ((Position) pos).getX() && this.y == ((Position) pos).getY();
	}
	
	@Override
	public int hashCode() {
		return this.x * HASH_CODE_MOLTIPLY_BY + this.y;
	}
}
