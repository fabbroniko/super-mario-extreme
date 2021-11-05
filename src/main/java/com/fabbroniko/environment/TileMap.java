package com.fabbroniko.environment;

import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TileMap implements Drawable {

	private static final int NO_TILE = -1;
	private static final int LAST_SOLID_TILE_INDEX = 6;
	private static final Vector2D TILE_SIZE = new Vector2D(120, 120);

	private final List<Tile> tiles = new ArrayList<>();
	private final Vector2D mapPosition = new Vector2D();
	private final Vector2D mapSize = new Vector2D();

	private int[][] map;
	private Vector2D minLimits;
	private Vector2D maxLimits;
	private final Vector2D canvasSize;

	private BufferedImage cachedTileMap;

	public TileMap(final ResourceManager resourceManager, final Map map, final Vector2D canvasSize) {
		this.canvasSize = canvasSize;

		loadTiles(resourceManager.getTileMapSet());
		loadMap(map, GameManager.getInstance().getCanvasSize());
	}

	private void loadTiles(final BufferedImage tileSet) {
		for (int currentX = 0; currentX < tileSet.getWidth(); currentX += TILE_SIZE.getRoundedX()) {
			TileType tt = TileType.BLOCKING;
			if(currentX / TILE_SIZE.getRoundedX() > LAST_SOLID_TILE_INDEX)
				tt = TileType.NON_BLOCKING;

			tiles.add(new Tile(tileSet.getSubimage(currentX, 0, TILE_SIZE.getRoundedX(), TILE_SIZE.getRoundedY()), tt));
		}
	}

	private void loadMap(final Map map, final Vector2D canvasSize) {
		final int nRows = map.getVerticalBlocks();
		final int nCols = map.getHorizontalBlocks();

		this.map = new int[nRows][nCols];
		mapSize.setVector2D(nCols * TILE_SIZE.getRoundedX(), nRows * TILE_SIZE.getRoundedY());
		minLimits = new Vector2D();
		maxLimits = new Vector2D(mapSize.getRoundedX() - canvasSize.getRoundedX(), mapSize.getRoundedY() - canvasSize.getRoundedY());

		for(int i = 0; i < nRows; i++) {
			for(int y = 0; y < nCols; y++) {
				this.map[i][y] = NO_TILE;
			}
		}

		for(final com.fabbroniko.resource.domain.Tile t : map.getTiles()) {
			this.map[t.getVerticalIndex()][t.getHorizontalIndex()] = t.getId();
		}
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

	public Vector2D getPosition() {
		return this.mapPosition.clone();
	}

	public Vector2D getMapSize() {
		return this.mapSize.clone();
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
	public TileInfo getTileType(final int xPoint, final int yPoint) {
		if(xPoint < 0 || yPoint < 0)
			return null;

		/* Calculate the index of the tile that includes the specified coordinate
		 * For example if the tile size is 30x30px and we want to check a point with coordinates 35,61 we would get
		 * 35/30 that rounded is 1 and 61/30 that rounded becomes 2, therefore we want to retried the tile at
		 * map[2, 1]
		 */
		final int yIndex = yPoint / TILE_SIZE.getRoundedY();
		final int xIndex = xPoint / TILE_SIZE.getRoundedX();

		if(yIndex >= map.length || xIndex >= map[yIndex].length) {
			return null;
		}

		TileType tileType = TileType.NON_BLOCKING;

		final int tileId = map[yIndex][xIndex];
		if(tileId != NO_TILE)
			tileType = tiles.get(tileId).getType();

		return new TileInfo(tileType, new Vector2D(xIndex * TILE_SIZE.getRoundedX(), yIndex * TILE_SIZE.getRoundedY()), TileMap.TILE_SIZE.clone());
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

		final BufferedImage tileMapImage = new BufferedImage(canvasSize.getRoundedX(), canvasSize.getRoundedY(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D tileMapGraphics = tileMapImage.createGraphics();

		final int startingXIndex = mapPosition.getRoundedX() / TILE_SIZE.getRoundedX();
		final int startingYIndex = mapPosition.getRoundedY() / TILE_SIZE.getRoundedY();

		final Vector2D basePosToDraw = new Vector2D();
		basePosToDraw.setX(basePosToDraw.getX() - (mapPosition.getX() % TILE_SIZE.getRoundedX()));
		basePosToDraw.setY(basePosToDraw.getY() - (mapPosition.getY() % TILE_SIZE.getRoundedY()));

		final Vector2D currentPosToDraw = new Vector2D(basePosToDraw.getX(), basePosToDraw.getY());
		int currentXIndexToDraw;
		int currentYIndexToDraw = startingYIndex;
		
		while (currentPosToDraw.getY() < canvasSize.getRoundedY()) {
			currentXIndexToDraw = startingXIndex;
			currentPosToDraw.setX(basePosToDraw.getX());
			while (currentPosToDraw.getX() < canvasSize.getRoundedX()) {
				if (map[currentYIndexToDraw][currentXIndexToDraw] != NO_TILE) {
					tileMapGraphics.drawImage(tiles.get(map[currentYIndexToDraw][currentXIndexToDraw]).getImage(), currentPosToDraw.getRoundedX(), currentPosToDraw.getRoundedY(), null);
				}
				currentPosToDraw.setX(currentPosToDraw.getX() + TILE_SIZE.getRoundedX());
				currentXIndexToDraw++;
			}
			currentPosToDraw.setY(currentPosToDraw.getY() + TILE_SIZE.getRoundedY());
			currentYIndexToDraw++;
		}

		cachedTileMap = tileMapImage;
		log.trace("tile_map,get_drawable_image,drawn_new_image");
		return cachedTileMap;
	}

	@Override
	public Vector2D getDrawingPosition() {
		return new Vector2D();
	}

	@AllArgsConstructor
	@Getter
	public static class TileInfo {

		private TileType tileType;
		private Vector2D origin;
		private Vector2D dimension;

		@Override
		public String toString() {
			return origin.toString();
		}
	}
}
