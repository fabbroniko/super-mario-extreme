package com.fabbroniko.collision;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.BoundingBox;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.map.TileMap;
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
			wantedPosition.position().setVector2D(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				gameObject.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}

			wantedPosition.position().setVector2D(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				gameObject.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			gameObject.notifyDeath();
		}

		for(final GameObject i : objects) {
			if(!i.equals(gameObject)) {
				wantedPosition.position().setVector2D(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY());
				if(i.getBoundingBox().intersects(wantedPosition)){
					gameObject.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, gameObject);
				}

				wantedPosition.position().setVector2D(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY());
				if(i.getBoundingBox().intersects(wantedPosition)) {
					gameObject.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, gameObject);
				}
			}
		}
	}
}
