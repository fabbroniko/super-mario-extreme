package com.fabbroniko.collision;

import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.sdi.annotation.Component;

import java.awt.Rectangle;
import java.util.List;

@Component
public class CollisionManager {

	private final TileMap tileMap;
	
	public CollisionManager(final TileMap tileMap) {
		this.tileMap = tileMap;
	}
	
	public void checkForCollisions(final GameObject gameObject, final Vector2D tmpOffsetPosition, final List<GameObject> objects) {
		final Vector2D offsetPosition = tmpOffsetPosition.clone();
		final Vector2D currentPosition = gameObject.getPosition().clone();
		final Vector2D objectDimension = gameObject.getDimension();

		final Rectangle wantedPosition = new Rectangle();
		try{
			wantedPosition.setBounds(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				gameObject.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}

			wantedPosition.setBounds(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				gameObject.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			gameObject.notifyDeath();
		}

		for(final GameObject i : objects) {
			if(!i.equals(gameObject)) {
				wantedPosition.setBounds(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
				if(i.getRectangle().intersects(wantedPosition)){
					gameObject.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, gameObject);
				}

				wantedPosition.setBounds(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
				if(i.getRectangle().intersects(wantedPosition)) {
					gameObject.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, gameObject);
				}
			}
		}
	}
}
