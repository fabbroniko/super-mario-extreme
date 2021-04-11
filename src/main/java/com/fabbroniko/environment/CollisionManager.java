package com.fabbroniko.environment;

import java.awt.Rectangle;
import java.util.List;

import com.fabbroniko.gameobjects.AbstractGameObject;

public class CollisionManager {

	private TileMap tileMap;
	private List<AbstractGameObject> objects;
	private Rectangle tmpRect;
	
	public CollisionManager(final TileMap tileMap, final List<AbstractGameObject> objects) {
		this.tileMap = tileMap;
		this.objects = objects;
		this.tmpRect = new Rectangle();
	}
	
	public void checkForCollisions(final AbstractGameObject obj, final Position tmpOffsetPosition) {
		final GameObjectBiDimensionalSpace tmpSpace = obj.getBiDimensionalSpace();
		final Position offsetPosition = tmpOffsetPosition.clone();
		
		try{
			// Controllo collisione con mappa Y.
			tmpRect.setBounds(tmpSpace.getPosition().getX(), tmpSpace.getPosition().getY() + offsetPosition.getY(), tmpSpace.getDimension().getWidth(), tmpSpace.getDimension().getHeight());
			if(this.tileMap.checkForMapCollision(tmpRect)){
				obj.handleMapCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION);
			}
		
			// Controllo collisione con mappa X.
			tmpRect.setBounds(tmpSpace.getPosition().getX() + offsetPosition.getX(), tmpSpace.getPosition().getY(), tmpSpace.getDimension().getWidth(), tmpSpace.getDimension().getHeight());
			if(this.tileMap.checkForMapCollision(tmpRect)){
				obj.handleMapCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION);
			}
		}catch (Exception e) {
			obj.notifyDeath();
		}
		
		
		// Controllo collisione con gli oggetti, faccio il ciclo
		for(final AbstractGameObject i:this.objects) {
			// Controllo di non fare il check sullo stesso oggetto in fase di testing.
			if(!i.equals(obj)) {
				// Controllo collisione con oggetti Y.
				tmpRect.setBounds(tmpSpace.getPosition().getX(), tmpSpace.getPosition().getY() + offsetPosition.getY(), tmpSpace.getDimension().getWidth(), tmpSpace.getDimension().getHeight());
				if(i.getBiDimensionalSpace().equals(tmpRect)){
					obj.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.BOTTOM_COLLISION : CollisionDirection.TOP_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getY() > 0 ? CollisionDirection.TOP_COLLISION : CollisionDirection.BOTTOM_COLLISION, obj);
				}
				
				// Controllo collisione con oggetti X.
				tmpRect.setBounds(tmpSpace.getPosition().getX() + offsetPosition.getX(), tmpSpace.getPosition().getY(), tmpSpace.getDimension().getWidth(), tmpSpace.getDimension().getHeight());
				if(i.getBiDimensionalSpace().equals(tmpRect)) {
					obj.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.RIGHT_COLLISION : CollisionDirection.LEFT_COLLISION, i);
					i.handleObjectCollisions(offsetPosition.getX() > 0 ? CollisionDirection.LEFT_COLLISION : CollisionDirection.RIGHT_COLLISION, obj);
				}
			}
		}
	}
}
