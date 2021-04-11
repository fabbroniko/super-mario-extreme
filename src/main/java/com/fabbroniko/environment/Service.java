package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.fabbroniko.error.ErrorManager;
import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.error.TileTypeError;

public final class Service {
	
	private Service() { }
	
	public static BufferedImage getImageFromName(final String location) {
		BufferedImage retImage;
		final Object classLoader = new TemporaryClassPathClass();

		try {
			retImage = ImageIO.read(classLoader.getClass().getResourceAsStream(location));
		} catch (Exception e) {
			ErrorManager.getInstance().notifyError(ErrorManager.ERROR_LOADING_IMAGE, "Unable to load the image named " + location);
			throw new ResourceNotFoundException(location);
		}
		
		return retImage;
	}
	
	/**
	 * Represents the type of a tile.
	 * @author com.fabbroniko
	 */
	public enum TileType {
		
		/**
		 * Represents a non-blocking tile.
		 */
		TILE_FREE(0),
		
		/**
		 * Represents a bloking tile.
		 */
		TILE_BLOCK(1);
		
		private final int tileType;
		
		TileType(final int i) {
			this.tileType = i;
		}
		
		private int getType() {
			return this.tileType;
		}
		
		/**
		 * Gets the TileType associated with the given index.
		 * @param index Value associated with each TileType.
		 * @return Returns the corresponding TileType.	
		 */
		public static TileType getTileType(final int index) {
			for (final TileType i:TileType.values()) {
				if (i.getType() == index) {
					return i;
				}
			}
			
			throw new TileTypeError(index);
		}
	}

	/*
	 * This is a temporary fix for the class loader issue I was having once picking up the project after a while.
	 * By using new Object to retrieve the class loader i was hitting the Bootstrap Class Loader and therefore I was unable to retrieve resources in my current ClassPath
	 * By creating this temporary class getClass().getClassLoader() will retrieve the correct Application ClassLoader that
	 * allows the application to find resources in the application classpath.
	 *
	 * TODO Refactor this once the code is cleaner and outside of a static method by using this.getClass() instead of creating an empty object.
	 */
	static class TemporaryClassPathClass {}
}
