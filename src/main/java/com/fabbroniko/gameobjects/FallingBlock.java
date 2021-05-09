package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.CollisionDirection;
import com.fabbroniko.environment.ObjectType;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.scene.GameScene;

public class FallingBlock extends AbstractGameObject {

	public FallingBlock(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, Animations.FALLING_BLOCK, objectID);
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && direction.equals(CollisionDirection.TOP_COLLISION)) {
			falling = true;
		}
	}
}
