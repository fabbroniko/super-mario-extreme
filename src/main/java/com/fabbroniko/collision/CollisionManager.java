package com.fabbroniko.collision;

import java.awt.Rectangle;
import java.util.List;

import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.sdi.annotation.Component;

@Component
public class CollisionManager {

	private final TileMap tileMap;
	
	public CollisionManager(final TileMap tileMap) {
		this.tileMap = tileMap;
	}
	
	public void checkForCollisions(final AbstractGameObject obj, final Vector2D tmpOffsetPosition, final List<AbstractGameObject> objects) {
		final Vector2D offsetPosition = tmpOffsetPosition.clone();
		final Vector2D currentPosition = obj.getPosition().clone();
		final Vector2D objectDimension = obj.getDimension();

		final Rectangle wantedPosition = new Rectangle();
		try{
			wantedPosition.setBounds(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				obj.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}

			wantedPosition.setBounds(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				obj.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			obj.notifyDeath();
		}

		for(final AbstractGameObject i : objects) {
			if(!i.equals(obj)) {
				wantedPosition.setBounds(currentPosition.getRoundedX(), currentPosition.getRoundedY() + offsetPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
				if(i.getRectangle().intersects(wantedPosition)){
					obj.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, obj);
				}

				wantedPosition.setBounds(currentPosition.getRoundedX() + offsetPosition.getRoundedX(), currentPosition.getRoundedY(), objectDimension.getRoundedX(), objectDimension.getRoundedY());
				if(i.getRectangle().intersects(wantedPosition)) {
					obj.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, obj);
				}
			}
		}
	}
}
