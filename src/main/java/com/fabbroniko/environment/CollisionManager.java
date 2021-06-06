package com.fabbroniko.environment;

import java.awt.Rectangle;
import java.util.List;

import com.fabbroniko.gameobjects.AbstractGameObject;

public class CollisionManager {

	private final TileMap tileMap;
	private final List<AbstractGameObject> objects;
	
	public CollisionManager(final TileMap tileMap, final List<AbstractGameObject> objects) {
		this.tileMap = tileMap;
		this.objects = objects;
	}
	
	public void checkForCollisions(final AbstractGameObject obj, final Position tmpOffsetPosition) {
		final Position offsetPosition = tmpOffsetPosition.clone();
		final Position currentPosition = obj.getPosition().clone();
		final Dimension objectDimension = obj.getDimension();

		final Rectangle wantedPosition = new Rectangle();
		try{
			wantedPosition.setBounds(currentPosition.getX(), currentPosition.getY() + offsetPosition.getY(), objectDimension.getWidth(), objectDimension.getHeight());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				obj.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}

			wantedPosition.setBounds(currentPosition.getX() + offsetPosition.getX(), currentPosition.getY(), objectDimension.getWidth(), objectDimension.getHeight());
			if(this.tileMap.checkForMapCollision(wantedPosition)){
				obj.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			obj.notifyDeath();
		}

		for(final AbstractGameObject i:this.objects) {
			if(!i.equals(obj)) {
				wantedPosition.setBounds(currentPosition.getX(), currentPosition.getY() + offsetPosition.getY(), objectDimension.getWidth(), objectDimension.getHeight());
				if(i.getRectangle().intersects(wantedPosition)){
					obj.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, obj);
				}

				wantedPosition.setBounds(currentPosition.getX() + offsetPosition.getX(), currentPosition.getY(), objectDimension.getWidth(), objectDimension.getHeight());
				if(i.getRectangle().intersects(wantedPosition)) {
					obj.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, obj);
				}
			}
		}
	}
}
