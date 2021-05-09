package com.fabbroniko.gameobjects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.Animation;
import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.CollisionDirection;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.ObjectType;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.GameManager;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;

/**
 * Represents the player's character.
 * @author com.fabbroniko
 */
public class Player extends AbstractGameObject implements KeyListener {

	private boolean animationJump;
	private boolean animationMove;
	private final GameScene gameScene;
	
	private final Dimension baseWindowSize;
	
	private final Animation animationWalk = new Animation(Animations.PLAYER_WALK);
	private final Animation animationStill = new Animation(Animations.PLAYER_STILL);
	private final Animation animationJumpA = new Animation(Animations.PLAYER_JUMP);

	public Player(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, Animations.PLAYER_JUMP, objectID);
		falling = true;
		animationJump = true;
		facingRight = true;
		this.objectType = ObjectType.TYPE_PLAYER;
		this.gameScene = gameScene;
		this.baseWindowSize = GameManager.getInstance().getCanvasSize();
	}
	
	@Override
	public void update() {
		super.update();
		tileMap.setPosition(this.getObjectPosition().getX() - (int) (baseWindowSize.getWidth() / 2), this.getObjectPosition().getY() - (int) (baseWindowSize.getHeight() / 2));
		if (death) {
			GameManager.getInstance().openScene(LostScene.class);
		}
		if (animationJump) {
			this.currentAnimation = animationJumpA;
		} else if (animationMove) {
			this.currentAnimation = animationWalk;
		} else {
			this.currentAnimation = animationStill;
		}
	}
	
	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
		super.handleMapCollisions(direction);
		
		if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			animationJump = false;
		}
	}
	 
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		if (!obj.getObjectType().equals(ObjectType.TYPE_INVISIBLE_BLOCK) || obj.getObjectType().equals(ObjectType.TYPE_INVISIBLE_BLOCK) && obj.currentAnimation.getAnimation().equals(Animations.INVISIBLEBLOCK_VISIBLE) || obj.objectType.equals(ObjectType.TYPE_INVISIBLE_BLOCK) && direction.equals(CollisionDirection.TOP_COLLISION)) { 
			super.handleObjectCollisions(direction, obj);
		}
		
		if (obj.getObjectType().equals(ObjectType.TYPE_ENEMY)) {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				jumping = true;
			} else {
				death = true;
			}
		} else if (obj.getObjectType().equals(ObjectType.TYPE_CASTLE)) {
			this.gameScene.levelFinished();
		} else {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				animationJump = false;
				groundHit = true;
			}
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getLeftMovementKeyCode()) {
			left = true;
			animationMove = true;
			facingRight = false;
		}
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getRightMovementKeyCode()) {
			right = true;
			animationMove = true;
			facingRight = true;
		}
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getJumpKeyCode() && !jumping && groundHit) {
			jumping = true;
			groundHit = false;
			currentJump = 0;
			animationJump = true;
			gameScene.getAudioManager().playEffect("jump");
		}
	}
 
	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getLeftMovementKeyCode()) {
			left = false; 
			animationMove = false;
		}
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getRightMovementKeyCode()) {
			right = false; 	
			animationMove = false;
		}
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getJumpKeyCode()) {
			jumping = false;
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {}
}
