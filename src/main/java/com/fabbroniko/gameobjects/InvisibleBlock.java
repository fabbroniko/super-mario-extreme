package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.CollisionDirection;
import com.fabbroniko.environment.ObjectType;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.scene.GameScene;

public class InvisibleBlock extends AbstractGameObject {

	public InvisibleBlock(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, Animations.INVISIBLEBLOCK_INVISIBLE, objectID);
		this.objectType = ObjectType.TYPE_INVISIBLE_BLOCK;
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) 
	{
		if (obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			this.setAnimation(Animations.INVISIBLEBLOCK_VISIBLE);
			this.gameScene.getAudioManager().playEffect("hit");
		}
	}
}
