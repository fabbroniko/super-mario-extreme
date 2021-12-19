package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.scene.GameScene;

public class Enemy extends AbstractGameObject implements AnimationListener {

	private static final Vector2D spriteDimension = new Vector2D(108, 192);
	private static final String spritePath = "/sprites/ghost.png";
	private static final String ENEMY_WALK_ANIMATION_NAME = "ENEMY_WALK";
	private static final String ENEMY_DEAD_ANIMATION_NAME = "ENEMY_DEAD";
	private final Animation deadAnimation;

	public Enemy(final TileMap tileMap, final GameScene gameScene, final Vector2D position) {
		super(tileMap, gameScene, position, spriteDimension);
		walkingSpeed = 300;

		this.currentStates.add(State.MOVING_DOWN);
		this.currentStates.add(State.MOVING_LEFT);

		deadAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.frameDuration(200)
				.animationListener(this)
				.name(ENEMY_DEAD_ANIMATION_NAME)
				.mirror()
				.build();

		setAnimation(Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(2)
				.frameDuration(100)
				.name(ENEMY_WALK_ANIMATION_NAME)
				.mirror()
				.build());
	}

	@Override
	public void collisionHandler(final CollisionManager.CollisionResult collisionResult) {
		if(!collisionResult.isHasCollided())
			return;

		if(collisionResult.getCollidedWith() == null) {
			if(collisionResult.getCollisionDirection().equals(CollisionDirection.LEFT_COLLISION)) {
				this.currentStates.remove(State.MOVING_LEFT);
				this.currentStates.add(State.MOVING_RIGHT);
				this.currentStates.add(State.FACING_RIGHT);
			} else if (collisionResult.getCollisionDirection().equals(CollisionDirection.RIGHT_COLLISION)) {
				this.currentStates.remove(State.FACING_RIGHT);
				this.currentStates.remove(State.MOVING_RIGHT);
				this.currentStates.add(State.MOVING_LEFT);
			}
		} else if (collisionResult.getCollidedWith() instanceof Player){
			if(collisionResult.getCollisionDirection().equals(CollisionDirection.TOP_COLLISION) && !currentAnimation.getName().equals(ENEMY_DEAD_ANIMATION_NAME)) {
				this.setAnimation(deadAnimation);
				this.gameScene.getAudioManager().playEffect("hit");
			}
		}
	}

	@Override
	public void animationFinished() {
		currentStates.clear();
		currentStates.add(State.DEAD);
	}
}
