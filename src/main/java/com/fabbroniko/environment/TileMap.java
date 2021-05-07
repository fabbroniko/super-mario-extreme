package com.fabbroniko.environment;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fabbroniko.environment.Service.TileType;
import com.fabbroniko.error.CorruptedFileError;
import com.fabbroniko.GameManager;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Map;

public class TileMap implements Drawable {

	// Tiles data
	private final Dimension tileSize;
	private BufferedImage tileSet;
	private int nRows;
	private int nCols;
		
	private final List<Tile> tiles;
	private int[][] map;
	private Dimension mapSize;
	private Position minLimits;
	private Position maxLimits;
	
	// Current position of the map
	private Position mapPosition;
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
		loadMap(map.getPath());
	}
	
	/**
	 * Splits the TileSet into the basic tiles and it stores them into and array of Tile
	 */
	private void loadTiles() {
		int i = 0;
		for (int currentY = 0; currentY < tileSet.getHeight(); currentY += tileSize.getHeight()) {
			for (int currentX = 0; currentX < tileSet.getWidth(); currentX += tileSize.getWidth()) {
				tiles.add(new Tile(tileSet.getSubimage(currentX, currentY, (int) tileSize.getWidth(), (int) tileSize.getHeight()), TileType.getTileType(i)));
			}
			i++;
		}
	}
	
	/**
	 * Loads the map stored in the specified file into a matrix composed by indexes of the tiles of the game.
	 * @param mapFile
	 */
	private void loadMap(final String mapFile) {
		final InputStream inputStream = getClass().getResourceAsStream(mapFile);
 		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			nRows = Integer.valueOf(bufferedReader.readLine());
			nCols = Integer.valueOf(bufferedReader.readLine());
		} catch (IOException e) {
			throw new CorruptedFileError(mapFile);
		}
		
		String[] values;
		
		map = new int[nRows][nCols];
		mapSize = new Dimension((int) (nCols * tileSize.getWidth()), (int) (nRows * tileSize.getHeight()));
		minLimits = new Position();
		maxLimits = new Position((int) (mapSize.getWidth() - baseWindowDimension.getWidth()), (int) (mapSize.getHeight() - baseWindowDimension.getHeight()));
		
		for (int i = 0; i < nRows; i++) {
			try {
				final String tmp = bufferedReader.readLine();
				if (tmp == null) {
					throw new CorruptedFileError(mapFile); 
				}
				values = tmp.split("\t");
				if (values.length < nCols) {
					throw new CorruptedFileError(mapFile);
				}
				for (int u = 0; u < nCols; u++) {
					map[i][u] = Integer.valueOf(values[u]);
				}
			} catch (IOException e) {
				throw new CorruptedFileError(mapFile);
			}
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
		if(getTileType(currentPoint).equals(TileType.TILE_BLOCK)) {
			return true;
		}
		
		// Checking the top-right corner
		currentPoint.setLocation(startingX + width, startingY);
		if(getTileType(currentPoint).equals(TileType.TILE_BLOCK)) {
			return true;
		}
		
		// Checking the bottom-left corner
		currentPoint.setLocation(startingX, startingY + height);
		if(getTileType(currentPoint).equals(TileType.TILE_BLOCK)) {
			return true;
		}
		
		// Checking the bottom-right corner
		currentPoint.setLocation(startingX + width, startingY + height);
		if(getTileType(currentPoint).equals(TileType.TILE_BLOCK)) {
			return true;
		}
		
		return false;
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
	 * Gets the type of the tile in the specified {@link Position Position}.
	 * @param pos Position containing the coordinates of a point in the map.
	 * @return Returns Returns the {@link TileType TileType} of the tile that contains the specified Position.
	 */
	public TileType getTileType(final Position pos) {
		return tiles.get(map[(int) (pos.getY() / tileSize.getHeight())][(int) (pos.getX() / tileSize.getWidth())]).getType();
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
	 * Gets the tile's dimension.
	 * @return Tile's Dimension.
	 */
	public Dimension getTileSize() {
		return this.tileSize;
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
	
	/**
	 * Determines the values needed for drawing the TileMap
	 */
	private void setDrawValues() {
		startingXIndex = (int) (mapPosition.getX() / tileSize.getWidth());
		startingYIndex = (int) (mapPosition.getY() / tileSize.getHeight());
		
		lastDrawablePosition.setX((int) (mapPosition.getX() + baseWindowDimension.getWidth()));
		lastDrawablePosition.setY((int) (mapPosition.getY() + baseWindowDimension.getHeight()));
	}
	
	/**
	 * @see com.fabbroniko.main.Drawable#update()
	 */
	@Override
	public void update() {}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		setDrawValues();

		final Position basePosToDraw = new Position();
		basePosToDraw.setX(basePosToDraw.getX() - (mapPosition.getX() % tileSize.getWidth()));
		basePosToDraw.setY(basePosToDraw.getY() - (mapPosition.getY() % tileSize.getHeight()));

		final Position currentPosToDraw = new Position(basePosToDraw.getX(), basePosToDraw.getY());
		int currentXIndexToDraw = startingXIndex;
		int currentYIndexToDraw = startingYIndex;
		
		while (currentPosToDraw.getY() < gDimension.getHeight()) {
			currentXIndexToDraw = startingXIndex;
			currentPosToDraw.setX(basePosToDraw.getX());
			while (currentPosToDraw.getX() < gDimension.getWidth()) {
				if (map[currentYIndexToDraw][currentXIndexToDraw] != 0) {
					g.drawImage(tiles.get(map[currentYIndexToDraw][currentXIndexToDraw]).getImage(), currentPosToDraw.getX(), currentPosToDraw.getY(), null);
				}
				currentPosToDraw.setX((int) (currentPosToDraw.getX() + tileSize.getWidth()));
				currentXIndexToDraw++;
			}
			currentPosToDraw.setY((int) (currentPosToDraw.getY() + tileSize.getHeight()));
			currentYIndexToDraw++;
		}
	}
}
