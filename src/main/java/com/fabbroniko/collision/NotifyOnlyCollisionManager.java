package com.fabbroniko.collision;

import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.sdi.annotation.Component;

import java.util.Optional;

@Component
public class NotifyOnlyCollisionManager implements CollisionManager {

	private final TileMap tileMap;

	public NotifyOnlyCollisionManager(final TileMap tileMap) {
		this.tileMap = tileMap;
    }

	@Override
	public Position calculateMovement(final GameObject subject, final Position offsetPosition) {
		final Position currentPosition = subject.getBoundingBox().position();
		final Dimension2D objectDimension = subject.getBoundingBox().dimension();

		final BoundingBox wantedPosition = new BoundingBox(new Vector2D(), objectDimension);
		wantedPosition.position().setPosition(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());

		// Check Y
		// Expected collision-less target heigh. 1320-105=1215
		Optional<BoundingBox> collisionY = checkForMapCollision(wantedPosition);
		if(collisionY.isPresent()){
			final CollisionDirection direction = offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION;
			subject.handleMapCollisions(direction);
			if(direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				offsetPosition.setPosition(offsetPosition.getX(), collisionY.get().position().getY() - objectDimension.height() - 1 - currentPosition.getY());
			} else {
				offsetPosition.setPosition(offsetPosition.getX(), (collisionY.get().position().getY() + collisionY.get().dimension().height()) - currentPosition.getY());
			}
		}

		// Check X
		wantedPosition.position().setPosition(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
		Optional<BoundingBox> collisionX = checkForMapCollision(wantedPosition);
		if(collisionX.isPresent()){
			final CollisionDirection direction = offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION;
			subject.handleMapCollisions(direction);
			if(direction.equals(CollisionDirection.RIGHT_COLLISION)) {
				offsetPosition.setPosition(collisionX.get().position().getX() - objectDimension.width() - 1 - currentPosition.getX(), offsetPosition.getY());
			} else {
				offsetPosition.setPosition((collisionX.get().position().getX() + collisionX.get().dimension().width()) - currentPosition.getX(), offsetPosition.getY());
			}
		}

		return offsetPosition;
	}

	private Optional<BoundingBox> checkForMapCollision(final BoundingBox boundingBox) throws ArrayIndexOutOfBoundsException{
		double startingX = boundingBox.position().getX();
		double startingY = boundingBox.position().getY();
		int width = boundingBox.dimension().width();
		int height = boundingBox.dimension().height();

		Optional<BoundingBox> topLeft = tileMap.getTileBoundingBoxAtPoint((int)startingX, (int)startingY);
		if(topLeft.isPresent()) {
			return topLeft;
		}

		Optional<BoundingBox> topRight = tileMap.getTileBoundingBoxAtPoint((int)startingX + width, (int)startingY);
		if(topRight.isPresent()) {
			return topRight;
		}

		Optional<BoundingBox> bottomLeft = tileMap.getTileBoundingBoxAtPoint((int)startingX, (int)startingY + height);
		if(bottomLeft.isPresent()) {
			return bottomLeft;
		}

		return tileMap.getTileBoundingBoxAtPoint((int)startingX + width, (int)startingY + height);
	}
}
