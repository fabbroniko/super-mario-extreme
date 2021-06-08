package com.fabbroniko.environment;

import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Map;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TileMap implements Drawable {

	private static final int NO_TILE = -1;
	private static final int LAST_SOLID_TILE_INDEX = 6;

	private final List<Tile> tiles = new ArrayList<>();
	private final Position mapPosition = new Position();

	private int[][] map;
	private Position minLimits;
	private Position maxLimits;
	private final Dimension tileSize;
	private final Dimension canvasSize;

	private BufferedImage cachedTileMap;

	public TileMap(final ResourceManager resourceManager, final Map map, final Dimension canvasSize) {
		this.tileSize = new Dimension(120, 120);
		this.canvasSize = canvasSize;

		loadTiles(resourceManager.getTileMapSet());
		loadMap(map, GameManager.getInstance().getCanvasSize());
	}

	private void loadTiles(final BufferedImage tileSet) {
		for (int currentX = 0; currentX < tileSet.getWidth(); currentX += tileSize.getWidth()) {
			TileType tt = TileType.BLOCKING;
			if(currentX/tileSize.getWidth() > LAST_SOLID_TILE_INDEX)
				tt = TileType.NON_BLOCKING;

			tiles.add(new Tile(tileSet.getSubimage(currentX, 0, tileSize.getWidth(), tileSize.getHeight()), tt));
		}
	}

	private void loadMap(final Map map, final Dimension canvasSize) {
		final int nRows = map.getVerticalBlocks();
		final int nCols = map.getHorizontalBlocks();

		this.map = new int[nRows][nCols];
		Dimension mapSize = new Dimension(nCols * tileSize.getWidth(), nRows * tileSize.getHeight());
		minLimits = new Position();
		maxLimits = new Position(mapSize.getWidth() - canvasSize.getWidth(), mapSize.getHeight() - canvasSize.getHeight());

		for(int i = 0; i < nRows; i++) {
			for(int y = 0; y < nCols; y++) {
				this.map[i][y] = NO_TILE;
			}
		}

		for(final com.fabbroniko.resource.domain.Tile t : map.getTiles()) {
			this.map[t.getVerticalIndex()][t.getHorizontalIndex()] = t.getId();
		}
	}

	public boolean checkForMapCollision(final Rectangle rect) throws ArrayIndexOutOfBoundsException{
		double startingX = rect.getLocation().getX();
		double startingY = rect.getLocation().getY();
		int width = rect.width;
		int height = rect.height;

		boolean outOfBounds = true;
		// Checking the top-left corner
		TileType tileType = getTileType((int)startingX, (int)startingY);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		} else if (tileType != null) {
			outOfBounds = false;
		}
		
		// Checking the top-right corner
		tileType = getTileType((int)startingX + width, (int)startingY);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		} else if (tileType != null) {
			outOfBounds = false;
		}
		
		// Checking the bottom-left corner
		tileType = getTileType((int)startingX, (int)startingY + height);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		} else if (tileType != null) {
			outOfBounds = false;
		}
		
		// Checking the bottom-right corner
		tileType = getTileType((int)startingX + width, (int)startingY + height);
		if (tileType != null) {
			outOfBounds = false;
		}

		if(outOfBounds)
			throw new ArrayIndexOutOfBoundsException();

		return TileType.BLOCKING.equals(tileType);
	}

	public void setPosition(final int x, final int y) {
		int adjustedX = x;
		int adjustedY = y;

		if (adjustedX < minLimits.getRoundedX()) {
			adjustedX = minLimits.getRoundedX();
		}
		if (adjustedY < minLimits.getRoundedY()) {
			adjustedY = minLimits.getRoundedY();
		}
		if (adjustedX > maxLimits.getRoundedX()) {
			adjustedX = maxLimits.getRoundedX();
		}
		if (adjustedY > maxLimits.getRoundedY()) {
			adjustedY = maxLimits.getRoundedY();
		}

		if(adjustedX != mapPosition.getX() || adjustedY != mapPosition.getY()) {
			cachedTileMap = null;
		}

		this.mapPosition.setX(adjustedX);
		this.mapPosition.setY(adjustedY);
	}

	public Position getPosition() {
		return this.mapPosition.clone();
	}

	/**
	 * Retrieves the {@link TileType TileType} of the tile at the specified coordinates.
	 * These coordinates can be any point in the map and it *doesn't have to* be rounded to the closest tile origin point
	 * (multiples of tile-size).
	 *
	 * If the point is outside the limits of the map, null is returned.
	 *
	 * @param xPoint The X coordinate element of the point we want to check.
	 * @param yPoint The Y coordinate element of the point we want to check.
	 * @return Returns the tile type at the specified point or null if out of the map limits.
	 */
	public TileType getTileType(final int xPoint, final int yPoint) {
		/* Calculate the index of the tile that includes the specified coordinate
		 * For example if the tile size is 30x30px and we want to check a point with coordinates 35,61 we would get
		 * 35/30 that rounded is 1 and 61/30 that rounded becomes 2, therefore we want to retried the tile at
		 * map[2, 1]
		 */
		final int yIndex = yPoint / tileSize.getHeight();
		final int xIndex = xPoint / tileSize.getWidth();

		if(yIndex >= map.length || xIndex >= map[yIndex].length) {
			return null;
		}

		final int tileId = map[yIndex][xIndex];
		if(tileId == NO_TILE)
			return TileType.NON_BLOCKING;

		return tiles.get(tileId).getType();
	}

	@Override
	public void update() {}

	// Add cache - if the position is not moved then use previously built tilemap
	@Override
	public BufferedImage getDrawableImage() {
		if(cachedTileMap != null) {
			log.trace("tile_map,get_drawable_image,using_cached_image");
			return cachedTileMap;
		}

		final BufferedImage tileMapImage = new BufferedImage(canvasSize.getWidth(), canvasSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D tileMapGraphics = tileMapImage.createGraphics();

		final int startingXIndex = mapPosition.getRoundedX() / tileSize.getWidth();
		final int startingYIndex = mapPosition.getRoundedY() / tileSize.getHeight();

		final Position basePosToDraw = new Position();
		basePosToDraw.setX(basePosToDraw.getX() - (mapPosition.getX() % tileSize.getWidth()));
		basePosToDraw.setY(basePosToDraw.getY() - (mapPosition.getY() % tileSize.getHeight()));

		final Position currentPosToDraw = new Position(basePosToDraw.getX(), basePosToDraw.getY());
		int currentXIndexToDraw;
		int currentYIndexToDraw = startingYIndex;
		
		while (currentPosToDraw.getY() < canvasSize.getHeight()) {
			currentXIndexToDraw = startingXIndex;
			currentPosToDraw.setX(basePosToDraw.getX());
			while (currentPosToDraw.getX() < canvasSize.getWidth()) {
				if (map[currentYIndexToDraw][currentXIndexToDraw] != NO_TILE) {
					tileMapGraphics.drawImage(tiles.get(map[currentYIndexToDraw][currentXIndexToDraw]).getImage(), currentPosToDraw.getRoundedX(), currentPosToDraw.getRoundedY(), null);
				}
				currentPosToDraw.setX(currentPosToDraw.getX() + tileSize.getWidth());
				currentXIndexToDraw++;
			}
			currentPosToDraw.setY(currentPosToDraw.getY() + tileSize.getHeight());
			currentYIndexToDraw++;
		}

		cachedTileMap = tileMapImage;
		log.trace("tile_map,get_drawable_image,drawn_new_image");
		return cachedTileMap;
	}

	@Override
	public Position getDrawingPosition() {
		return new Position();
	}
}
