package com.fabbroniko.environment;

/**
 * Class representing a bi-dimensional dimension.
 * @author com.fabbroniko
 */
public class Dimension implements Cloneable {
	
	private int width;
	private int height;
	
	/**
	 * Constructs a new Basic Dimension.
	 */
	public Dimension() {
		this(Service.BASE_DIMENSION.clone());
	}
	
	/**
	 * Constructs a new Dimension starting from another.
	 * @param dim Dimension from which it will be copied.
	 */
	public Dimension(final Dimension dim) {
		this.width = dim.getWidth();
		this.height = dim.getHeight();
	}
	
	/**
	 * Constructs a dimension starting from the given width and height.
	 * @param widthP Width.
	 * @param heightP Height.
	 */
	public Dimension(final int widthP, final int heightP) {
		this.width = widthP;
		this.height = heightP;
	}
	
	/**
	 * Constructs a dimension starting from a {@link java.awt.Dimension Dimension}.
	 * @param dim Dimension from which it will be constructed.
	 */
	public Dimension(final java.awt.Dimension dim) {
		this.width = (int) dim.getWidth();
		this.height = (int) dim.getHeight();
	}
	
	/**
	 * Gets the width.
	 * @return Returns the width.
	 */
	public int getWidth() { 
		return this.width; 
	}
	
	/**
	 * Gets the height.
	 * @return Returns the height.
	 */
	public int getHeight() { 
		return this.height; 
	}
	
	/**
	 * Sets the width.
	 * @param widthP Width.
	 */
	public void setWidth(final int widthP) { 
		this.width = widthP;
	}
	
	/**
	 * Sets the height.
	 * @param heightP Height.
	 */
	public void setHeight(final int heightP) { 
		this.height = heightP; 
	}
	
	/**
	 * Sets the dimension starting from the given Dimension.
	 * @param dim Dimensions from which it will be copied.
	 */
	public void setDimension(final Dimension dim) {
		this.width = dim.getWidth();
		this.height = dim.getHeight();
	}
	
	/**
	 * Sets the dimension with the given width and height.
	 * @param widthP Width.
	 * @param heightP Height.
	 */
	public void setDimension(final int widthP, final int heightP) {
		this.width = widthP;
		this.height = heightP;
	}
	
	@Override
	public Dimension clone() {
		return new Dimension(this);
	}
}
