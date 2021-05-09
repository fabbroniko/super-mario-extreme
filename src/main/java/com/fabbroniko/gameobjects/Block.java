package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.CollisionDirection;
import com.fabbroniko.environment.ObjectType;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.environment.Animation.AnimationListener;
import com.fabbroniko.scene.GameScene;

/**
 * Represents the simplest block in the game.
 * It can be break if hit from the bottom.
 * @author com.fabbroniko
 */
public class Block extends AbstractGameObject implements AnimationListener {

	public Block(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, Animations.BLOCK_NORMAL, objectID);
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (obj.getObjectType().equals(ObjectType.TYPE_PLAYER) && direction.equals(CollisionDirection.BOTTOM_COLLISION) && !currentAnimation.equals(Animations.BLOCK_BREAKING)) { // TODO check this equals of 2 different classes
			this.setAnimation(Animations.BLOCK_BREAKING);
			currentAnimation.setAnimationListener(this);
			gameScene.getAudioManager().playEffect("breaking");
		}
	}

	@Override
	public void animationFinished() {
		death = true;
	}
}
