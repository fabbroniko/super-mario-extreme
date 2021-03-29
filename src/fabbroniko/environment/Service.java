package fabbroniko.environment;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import fabbroniko.error.ErrorManager;
import fabbroniko.error.ResourceNotFoundError;
import fabbroniko.error.TileTypeError;
import fabbroniko.gamestatemanager.GameStateManager;

/**
 * Service Class used to perform environmental-operations.
 * @author fabbroniko
 */
public final class Service {

	/**
	 * Basic tile dimension.
	 */
	public static final Dimension TILE_DIMENSION = new Dimension(30, 30);
	
	/**
	 * Represents the lower dimension that can be used.
	 */
	public static final Dimension BASE_DIMENSION = new Dimension(0, 0);
	
	/**
	 * Base Position.
	 */
	public static final Position ORIGIN = new Position(0, 0);
	
	private Service() { }
	
	/**
	 * Gets a new centered position (X Coord) starting from the BASE_SIZE of the game and the dimension of the view.
	 * @param viewDimension dimension of the view that has to be centered
	 * @return A new Centered Position
	 */
	public static Position getXCentredPosition(final Dimension viewDimension) {
		final Position centredPosition = ORIGIN.clone();
		centredPosition.setX((GameStateManager.getInstance().getBaseWindowSize().getWidth() - viewDimension.getWidth()) / 2);
		return centredPosition;
	}
	
	public static boolean intersects(final Rectangle first, final Point point) {
		return first.contains(point);
	}
	
	public static BufferedImage getImageFromName(final String location) {
		BufferedImage retImage;
		final Object classLoader = new Object();
		
		try {
			retImage = ImageIO.read(classLoader.getClass().getResourceAsStream(location));
		} catch (Exception e) {
			ErrorManager.getInstance().notifyError(ErrorManager.ERROR_LOADING_IMAGE, "Unable to load the image named " + location);
			throw new ResourceNotFoundError(location);
		}
		
		return retImage;
	}
	
	/**
	 * Represents the type of a tile.
	 * @author fabbroniko
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
		
		private int tileType;
		
		private TileType(final int i) {
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
}
