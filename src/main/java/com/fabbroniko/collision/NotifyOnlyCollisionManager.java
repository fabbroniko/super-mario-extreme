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
public class NotifyOnlyCollisionManager implements CollisionManager {

	private final TileMap tileMap;
	private final GameScene gameScene;

	public NotifyOnlyCollisionManager(final TileMap tileMap, final GameScene gameScene) {
		this.tileMap = tileMap;
        this.gameScene = gameScene;
    }

	@Override
	public Vector2D calculateMovement(final GameObject subject, final Vector2D offsetPosition) {
		final Position currentPosition = subject.getBoundingBox().position();
		final Dimension2D objectDimension = subject.getBoundingBox().dimension();

		final BoundingBox wantedPosition = new BoundingBox(new Vector2D(), objectDimension);
		wantedPosition.position().setPosition(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
		if(checkForMapCollision(wantedPosition)){
			subject.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
		}

		wantedPosition.position().setPosition(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
		if(checkForMapCollision(wantedPosition)){
			subject.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
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

		return offsetPosition;
	}

	private boolean checkForMapCollision(final BoundingBox boundingBox) throws ArrayIndexOutOfBoundsException{
		double startingX = boundingBox.position().getX();
		double startingY = boundingBox.position().getY();
		int width = boundingBox.dimension().width();
		int height = boundingBox.dimension().height();

		// Checking the top-left corner
		TileType tileType = tileMap.getTileType((int)startingX, (int)startingY);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		}

		// Checking the top-right corner
		tileType = tileMap.getTileType((int)startingX + width, (int)startingY);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		}

		// Checking the bottom-left corner
		tileType = tileMap.getTileType((int)startingX, (int)startingY + height);
		if(TileType.BLOCKING.equals(tileType)) {
			return true;
		}

		// Checking the bottom-right corner
		tileType = tileMap.getTileType((int)startingX + width, (int)startingY + height);
		return TileType.BLOCKING.equals(tileType);
	}
}
