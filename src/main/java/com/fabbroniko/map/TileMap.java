package com.fabbroniko.map;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.DrawableResource;
import com.fabbroniko.main.DrawableResourceImpl;
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
	private final Vector2D mapPosition = new Vector2D();

	private int[][] map;
	private Vector2D minLimits;
	private Vector2D maxLimits;
	private final Vector2D tileSize;
	private final Dimension2D canvasSize;

	private BufferedImage cachedTileMap;

	public TileMap(final ResourceManager resourceManager, final Map map, final Dimension2D canvasSize) {
		this.tileSize = new Vector2D(120, 120);
		this.canvasSize = canvasSize;

		loadTiles(resourceManager.getTileMapSet());
		loadMap(map, canvasSize);
	}

	private void loadTiles(final BufferedImage tileSet) {
		for (int currentX = 0; currentX < tileSet.getWidth(); currentX += tileSize.getRoundedX()) {
			TileType tt = TileType.BLOCKING;
			if(currentX/tileSize.getRoundedX() > LAST_SOLID_TILE_INDEX)
				tt = TileType.NON_BLOCKING;

			tiles.add(new Tile(tileSet.getSubimage(currentX, 0, tileSize.getRoundedX(), tileSize.getRoundedY()), tt));
		}
	}

	private void loadMap(final Map map, final Dimension2D canvasSize) {
		final int nRows = map.getVerticalBlocks();
		final int nCols = map.getHorizontalBlocks();

		this.map = new int[nRows][nCols];
		Vector2D mapSize = new Vector2D(nCols * tileSize.getRoundedX(), nRows * tileSize.getRoundedY());
		minLimits = new Vector2D();
		maxLimits = new Vector2D(mapSize.getRoundedX() - canvasSize.width(), mapSize.getRoundedY() - canvasSize.height());

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

	public Vector2D getPosition() {
		return this.mapPosition.clone();
	}

	public TileType getTileType(final int xPoint, final int yPoint) {
		final int yIndex = yPoint / tileSize.getRoundedY();
		final int xIndex = xPoint / tileSize.getRoundedX();

		if(yIndex >= map.length || xIndex >= map[yIndex].length) {
			return null;
		}

		final int tileId = map[yIndex][xIndex];
		if(tileId == NO_TILE)
			return TileType.NON_BLOCKING;

		return tiles.get(tileId).type();
	}

	// Add cache - if the position is not moved then use previously built tilemap
	@Override
	public DrawableResource getDrawableResource() {
		if(cachedTileMap != null) {
			log.trace("tile_map,get_drawable_image,using_cached_image");
			return new DrawableResourceImpl(cachedTileMap, new ImmutablePosition(0, 0));
		}

		final BufferedImage tileMapImage = new BufferedImage(canvasSize.width(), canvasSize.height(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D tileMapGraphics = tileMapImage.createGraphics();

		final int startingXIndex = mapPosition.getRoundedX() / tileSize.getRoundedX();
		final int startingYIndex = mapPosition.getRoundedY() / tileSize.getRoundedY();

		final Vector2D basePosToDraw = new Vector2D();
		basePosToDraw.setX(basePosToDraw.getX() - (mapPosition.getX() % tileSize.getRoundedX()));
		basePosToDraw.setY(basePosToDraw.getY() - (mapPosition.getY() % tileSize.getRoundedY()));

		final Vector2D currentPosToDraw = new Vector2D(basePosToDraw.getX(), basePosToDraw.getY());
		int currentXIndexToDraw;
		int currentYIndexToDraw = startingYIndex;
		
		while (currentPosToDraw.getY() < canvasSize.height()) {
			currentXIndexToDraw = startingXIndex;
			currentPosToDraw.setX(basePosToDraw.getX());
			while (currentPosToDraw.getX() < canvasSize.width()) {
				if (map[currentYIndexToDraw][currentXIndexToDraw] != NO_TILE) {
					tileMapGraphics.drawImage(tiles.get(map[currentYIndexToDraw][currentXIndexToDraw]).image(), currentPosToDraw.getRoundedX(), currentPosToDraw.getRoundedY(), null);
				}
				currentPosToDraw.setX(currentPosToDraw.getX() + tileSize.getRoundedX());
				currentXIndexToDraw++;
			}
			currentPosToDraw.setY(currentPosToDraw.getY() + tileSize.getRoundedY());
			currentYIndexToDraw++;
		}

		cachedTileMap = tileMapImage;
		log.trace("tile_map,get_drawable_image,drawn_new_image");
		return new DrawableResourceImpl(cachedTileMap, new ImmutablePosition(0, 0));
	}
}
