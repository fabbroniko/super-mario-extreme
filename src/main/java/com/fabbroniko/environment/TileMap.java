package com.fabbroniko.environment;

import com.fabbroniko.GameManager;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileMap implements Drawable {

	// Tiles data
	private final Dimension tileSize;
	private final BufferedImage tileSet;

	private final List<Tile> tiles;
	private int[][] map;
	private Dimension mapSize;
	private Position minLimits;
	private Position maxLimits;

	// Current position of the map
	private final Position mapPosition;
	private final Position lastDrawablePosition;
	private int startingXIndex;
	private int startingYIndex;

	private final Dimension baseWindowDimension;

	public TileMap(final ResourceManager resourceManager, final Map map) {
		tiles = new ArrayList<>();
		tileSize = new Dimension(30, 30);
		baseWindowDimension = GameManager.getInstance().getCanvasSize();
		
		mapPosition = new Position();
		lastDrawablePosition = new Position();
		
		this.tileSet = resourceManager.getTileMapSet();
		
		loadTiles();
		loadMap(map);
	}
	
	/**
	 * Splits the TileSet into the basic tiles and it stores them into and array of Tile
	 */
	private void loadTiles() {
		for (int currentX = 0; currentX < tileSet.getWidth(); currentX += tileSize.getWidth()) {
			TileType tt = TileType.BLOCKING;
			if(currentX/tileSize.getWidth() > 4)
				tt = TileType.NON_BLOCKING;

			tiles.add(new Tile(tileSet.getSubimage(currentX, 0, tileSize.getWidth(), tileSize.getHeight()), tt));
		}
	}

	private void loadMap(final Map map) {
		int nRows = map.getVerticalBlocks();
		int nCols = map.getHorizontalBlocks();

		this.map = new int[nRows][nCols];
		mapSize = new Dimension(nCols * tileSize.getWidth(), nRows * tileSize.getHeight());
		minLimits = new Position();
		maxLimits = new Position(mapSize.getWidth() - baseWindowDimension.getWidth(), mapSize.getHeight() - baseWindowDimension.getHeight());

		for(int i = 0; i < nRows; i++) {
			for(int y = 0; y < nCols; y++) {
				this.map[i][y] = 5;
			}
		}

		for(final com.fabbroniko.resource.domain.Tile t : map.getTiles()) {
			this.map[t.getVerticalIndex()][t.getHorizontalIndex()] = t.getId();
		}
	}

	public void setPosition(final Position position) {
		this.mapPosition.setPosition(position.getX(), position.getY());

		adjustCoordinates();
	}
	
	/**
	 * Gets the map's dimension.
	 * @return Returns a new instance of the map's dimension.
	 */
	public Dimension getDimension() {
		return mapSize;
	}
	
	public boolean checkForMapCollision(final Rectangle rect) throws ArrayIndexOutOfBoundsException{
		final Point currentPoint = new Point();
		double startingX = rect.getLocation().getX();
		double startingY = rect.getLocation().getY();
		int width = rect.width;
		int height = rect.height;
		
		// Checking the top-left corner
		currentPoint.setLocation(startingX, startingY);
		if(getTileType(currentPoint).equals(TileType.BLOCKING)) {
			return true;
		}
		
		// Checking the top-right corner
		currentPoint.setLocation(startingX + width, startingY);
		if(getTileType(currentPoint).equals(TileType.BLOCKING)) {
			return true;
		}
		
		// Checking the bottom-left corner
		currentPoint.setLocation(startingX, startingY + height);
		if(getTileType(currentPoint).equals(TileType.BLOCKING)) {
			return true;
		}
		
		// Checking the bottom-right corner
		currentPoint.setLocation(startingX + width, startingY + height);
		return getTileType(currentPoint).equals(TileType.BLOCKING);
	}
	
	/**
	 * Sets the map's position. If it goes out of bounds, its position will be automatically adjusted.
	 * @param x X Coordinate.
	 * @param y Y Coordinate.
	 */
	public void setPosition(final int x, final int y) {
		this.mapPosition.setX(x);
		this.mapPosition.setY(y);
		adjustCoordinates();
	}
	
	/**
	 * Gets the current map's position.
	 * @return Return's a new instance of the map's location.
	 */
	public Position getPosition() {
		return this.mapPosition.clone();
	}
	
	/**
	 * Gets the type of the tile in the specified {@link Point Point}.
	 * @param point Point containing the coordinates of a point in the map. 
	 * @return Returns the {@link TileType TileType} of the tile that contains the specified point.
	 */
	public TileType getTileType(final Point point) {
		return tiles.get(map[(int) (point.getY() / tileSize.getHeight())][(int) (point.getX() / tileSize.getWidth())]).getType();
	}
	
	/**
	 * Checks if the position is valid. If an invalid position has been set, it will be adjusted.
	 */
	private void adjustCoordinates() {
		if (mapPosition.getX() < minLimits.getX()) {
			mapPosition.setX(minLimits.getX());
		}
		if (mapPosition.getY() < minLimits.getY()) {
			mapPosition.setY(minLimits.getY());
		}
		if (mapPosition.getX() > maxLimits.getX()) {
			mapPosition.setX(maxLimits.getX());
		}
		if (mapPosition.getY() > maxLimits.getY()) {
			mapPosition.setY(maxLimits.getY());
		}
	}

	private void setDrawValues() {
		startingXIndex = mapPosition.getX() / tileSize.getWidth();
		startingYIndex = mapPosition.getY() / tileSize.getHeight();
		
		lastDrawablePosition.setX(mapPosition.getX() + baseWindowDimension.getWidth());
		lastDrawablePosition.setY(mapPosition.getY() + baseWindowDimension.getHeight());
	}

	@Override
	public void update() {}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		setDrawValues();

		final Position basePosToDraw = new Position();
		basePosToDraw.setX(basePosToDraw.getX() - (mapPosition.getX() % tileSize.getWidth()));
		basePosToDraw.setY(basePosToDraw.getY() - (mapPosition.getY() % tileSize.getHeight()));

		final Position currentPosToDraw = new Position(basePosToDraw.getX(), basePosToDraw.getY());
		int currentXIndexToDraw;
		int currentYIndexToDraw = startingYIndex;
		
		while (currentPosToDraw.getY() < gDimension.getHeight()) {
			currentXIndexToDraw = startingXIndex;
			currentPosToDraw.setX(basePosToDraw.getX());
			while (currentPosToDraw.getX() < gDimension.getWidth()) {
				if (map[currentYIndexToDraw][currentXIndexToDraw] != 5) {
					g.drawImage(tiles.get(map[currentYIndexToDraw][currentXIndexToDraw]).getImage(), currentPosToDraw.getX(), currentPosToDraw.getY(), null);
				}
				currentPosToDraw.setX(currentPosToDraw.getX() + tileSize.getWidth());
				currentXIndexToDraw++;
			}
			currentPosToDraw.setY(currentPosToDraw.getY() + tileSize.getHeight());
			currentYIndexToDraw++;
		}
	}
}
