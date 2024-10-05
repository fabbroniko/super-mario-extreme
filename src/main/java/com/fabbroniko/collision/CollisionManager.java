package com.fabbroniko.collision;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.map.TileType;
import com.fabbroniko.sdi.annotation.Component;

import java.util.List;

@Component
public class CollisionManager {

	private final TileMap tileMap;
	
	public CollisionManager(final TileMap tileMap) {
		this.tileMap = tileMap;
	}
	
	public void checkForCollisions(final GameObject gameObject, final Vector2D tmpOffsetPosition, final List<GameObject> objects) {
		final Vector2D offsetPosition = tmpOffsetPosition.clone();
		final Position currentPosition = gameObject.getBoundingBox().position();
		final Dimension2D objectDimension = gameObject.getBoundingBox().dimension();

		final BoundingBox wantedPosition = new BoundingBox(new Vector2D(), objectDimension);
		try{
			wantedPosition.position().setPosition(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
			if(checkForMapCollision(wantedPosition)){
				gameObject.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}

			wantedPosition.position().setPosition(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
			if(checkForMapCollision(wantedPosition)){
				gameObject.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			gameObject.notifyDeath();
		}

		for(final GameObject i : objects) {
			if(!i.equals(gameObject)) {
				wantedPosition.position().setPosition(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
				if(i.getBoundingBox().intersects(wantedPosition)){
					gameObject.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, gameObject);
				}

				wantedPosition.position().setPosition(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
				if(i.getBoundingBox().intersects(wantedPosition)) {
					gameObject.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, gameObject);
				}
			}
		}
	}

	private boolean checkForMapCollision(final BoundingBox boundingBox) throws ArrayIndexOutOfBoundsException{
		double startingX = boundingBox.position().getX();
		double startingY = boundingBox.position().getY();
		int width = boundingBox.dimension().width();
		int height = boundingBox.dimension().height();

		boolean outOfBounds = true;
		// Checking the top-left corner
		TileType tileType = tileMap.getTileType((int)startingX, (int)startingY);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		} else if (tileType != null) {
			outOfBounds = false;
		}

		// Checking the top-right corner
		tileType = tileMap.getTileType((int)startingX + width, (int)startingY);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		} else if (tileType != null) {
			outOfBounds = false;
		}

		// Checking the bottom-left corner
		tileType = tileMap.getTileType((int)startingX, (int)startingY + height);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		} else if (tileType != null) {
			outOfBounds = false;
		}

		// Checking the bottom-right corner
		tileType = tileMap.getTileType((int)startingX + width, (int)startingY + height);
		if (tileType != null) {
			outOfBounds = false;
		}

		if(outOfBounds)
			throw new ArrayIndexOutOfBoundsException();

		return TileType.BLOCKING.equals(tileType);
	}
}
