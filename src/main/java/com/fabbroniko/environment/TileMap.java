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

	private static final int NO_TILE = -1;
	private static final int LAST_SOLID_TILE_INDEX = 6;

	private final List<Tile> tiles = new ArrayList<>();
	private final Position mapPosition = new Position();

	private int[][] map;
	private Position minLimits;
	private Position maxLimits;
	private final Dimension tileSize;

	public TileMap(final ResourceManager resourceManager, final Map map) {
		tileSize = new Dimension(120, 120);

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

	public void setPosition(final int x, final int y) {
		this.mapPosition.setX(x);
		this.mapPosition.setY(y);

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

	public Position getPosition() {
		return this.mapPosition.clone();
	}

	public TileType getTileType(final Point point) {
		final int tileId = map[(int) (point.getY() / tileSize.getHeight())][(int) (point.getX() / tileSize.getWidth())];
		if(tileId == NO_TILE)
			return TileType.NON_BLOCKING;

		return tiles.get(tileId).getType();
	}

	@Override
	public void update() {}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		final int startingXIndex = mapPosition.getX() / tileSize.getWidth();
		final int startingYIndex = mapPosition.getY() / tileSize.getHeight();

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
				if (map[currentYIndexToDraw][currentXIndexToDraw] != NO_TILE) {
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
