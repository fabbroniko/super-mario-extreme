package com.fabbroniko.collision;

import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.map.TileType;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.sdi.annotation.Component;

@Component
public class DefaultCollisionManager implements CollisionManager {

	private final TileMap tileMap;
	private final GameScene gameScene;

	public DefaultCollisionManager(final TileMap tileMap, final GameScene gameScene) {
		this.tileMap = tileMap;
        this.gameScene = gameScene;
    }

	@Override
	public void calculateMovement(final GameObject subject, final Vector2D offsetPosition) {
		final Position currentPosition = subject.getBoundingBox().position();
		final Dimension2D objectDimension = subject.getBoundingBox().dimension();

		final BoundingBox wantedPosition = new BoundingBox(new Vector2D(), objectDimension);
		try{
			wantedPosition.position().setPosition(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
			if(checkForMapCollision(wantedPosition)){
				subject.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}

			wantedPosition.position().setPosition(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
			if(checkForMapCollision(wantedPosition)){
				subject.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			subject.notifyDeath();
		}

		for(final GameObject gameObject : gameScene.gameObjects()) {
			if(!gameObject.equals(subject)) {
				wantedPosition.position().setPosition(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
				if(gameObject.getBoundingBox().intersects(wantedPosition)){
					subject.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, gameObject);
					gameObject.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, subject);
				}

				wantedPosition.position().setPosition(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
				if(gameObject.getBoundingBox().intersects(wantedPosition)) {
					subject.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, gameObject);
					gameObject.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, subject);
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
