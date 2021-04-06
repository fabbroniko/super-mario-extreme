package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

import com.fabbroniko.environment.Service.TileType;

/**
 * Represents one tile of the map.
 * @author nicola.fabbrini
 *
 */
public class Tile {

	private final BufferedImage tileImage;
	private final TileType type;
	
	/**
	 * Constructs a new tile.
	 * @param img Tile Image
	 * @param typeP Tile type (e.g. BLOCKED or UNBLOCKED)
	 */
	public Tile(final BufferedImage img, final TileType typeP) {
		this.tileImage = img;
		this.type = typeP;
	}
	
	/**
	 * Image Getter.
	 * @return Returns the tile's image.
	 */
	public BufferedImage getImage() {
		return tileImage;
	}
	
	/**
	 * Type Getter.
	 * @return Returns the tile's type.
	 */
	public TileType getType() {
		return this.type;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(o == null || !(o instanceof Tile)){
			return false;
		}
		final Tile obj = (Tile)o;
		return this.type.equals(obj.type) && this.tileImage.equals(obj.tileImage);
	}
	
	@Override
	public int hashCode() {
		return (this.tileImage.hashCode() & this.type.hashCode()) ^ (this.tileImage.hashCode() | this.type.hashCode());
	}
}
