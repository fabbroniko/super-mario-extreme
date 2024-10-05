package com.fabbroniko.map;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.ui.Drawable;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

@Component
public class TileMap implements Drawable {

	private static final int NO_TILE = -1;

	private final List<Tile> tiles;
	private final int[][] map;
	private final Dimension2D tileSize;
	private final Dimension2D canvasSize;

	private BufferedImage cachedTileMap;
	private final Position maxLimits;
	private final Vector2D mapPosition = new Vector2D();
	private final Position origin = new Vector2D();

	public TileMap(@Qualifier("canvasSize") final Dimension2D canvasSize,
				   @Qualifier("tileDimension") final Dimension2D tileSize,
				   final TileLoader tileLoader,
				   final MapLoader mapLoader) {

		this.tileSize = tileSize;
		this.canvasSize = canvasSize;

		this.tiles = tileLoader.load();
		this.map = mapLoader.load();

		final int nCols = map.length;
		final int nRows = map[0].length;
		this.maxLimits = new Vector2D(
			(nCols * tileSize.width()) - canvasSize.width(),
			(nRows * tileSize.height()) - canvasSize.height()
		);
	}

	public void setPosition(final int x, final int y) {
		int adjustedX = x;
		int adjustedY = y;

		if (adjustedX < origin.getX()) {
			adjustedX = origin.getRoundedX();
		}
		if (adjustedY < origin.getY()) {
			adjustedY = origin.getRoundedY();
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

		this.mapPosition.setPosition(adjustedX, adjustedY);
	}

	public Vector2D getPosition() {
		return this.mapPosition.clone();
	}

	public TileType getTileType(final int xPoint, final int yPoint) {
		final int yIndex = yPoint / tileSize.height();
		final int xIndex = xPoint / tileSize.width();

		if(yIndex >= map.length || xIndex >= map[yIndex].length) {
			return null;
		}

		final int tileId = map[yIndex][xIndex];
		if(tileId == NO_TILE)
			return TileType.NON_BLOCKING;

		return tiles.get(tileId).type();
	}

	@Override
	public DrawableResource getDrawableResource() {
		if(cachedTileMap != null) {
			return new DrawableResourceImpl(cachedTileMap, new ImmutablePosition(0, 0));
		}

		final BufferedImage tileMapImage = new BufferedImage(canvasSize.width(), canvasSize.height(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D tileMapGraphics = tileMapImage.createGraphics();

		final int startingXIndex = mapPosition.getRoundedX() / tileSize.width();
		final int startingYIndex = mapPosition.getRoundedY() / tileSize.height();

		final Vector2D basePosToDraw = new Vector2D();
		basePosToDraw.setX(basePosToDraw.getX() - (mapPosition.getX() % tileSize.width()));
		basePosToDraw.setY(basePosToDraw.getY() - (mapPosition.getY() % tileSize.height()));

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
				currentPosToDraw.setX(currentPosToDraw.getX() + tileSize.width());
				currentXIndexToDraw++;
			}
			currentPosToDraw.setY(currentPosToDraw.getY() + tileSize.height());
			currentYIndexToDraw++;
		}

		cachedTileMap = tileMapImage;
		return new DrawableResourceImpl(cachedTileMap, new ImmutablePosition(0, 0));
	}
}
