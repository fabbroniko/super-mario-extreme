package com.fabbroniko.gameobjects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.*;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import lombok.extern.log4j.Log4j2;

/**
 * Represents the player's character.
 * @author com.fabbroniko
 */
@Log4j2
public class Player extends AbstractGameObject implements KeyListener {

	private static final Vector2D spriteDimension = new Vector2D(112, 104);
	private static final String spritePath = "/sprites/mario.png";

	public static final String MARIO_IDLE_ANIMATION_NAME = "MARIO_IDLE";
	public static final String MARIO_WALK_ANIMATION_NAME = "MARIO_WALK";
	public static final String MARIO_JUMP_ANIMATION_NAME = "MARIO_JUMP";

	private boolean animationJump;
	private boolean animationMove;
	private final GameScene gameScene;
	
	private final Vector2D baseWindowSize;
	
	private final Animation walkAnimation;
	private final Animation idleAnimation;
	private final Animation jumpAnimation;

	public Player(final TileMap tileMap,
				  final GameScene gameScene,
				  final ResourceManager resourceManager,
				  final AudioManager audioManager,
				  final Vector2D position) {
		super(tileMap, gameScene, resourceManager, audioManager, position, spriteDimension);

		falling = true;
		animationJump = true;
		this.gameScene = gameScene;
		this.baseWindowSize = GameManager.getInstance().getCanvasSize();

		idleAnimation = Animation.builder()
				.spriteSet(resourceManager.loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(MARIO_IDLE_ANIMATION_NAME)
				.mirror()
				.build();

		jumpAnimation = Animation.builder()
				.spriteSet(resourceManager.loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.name(MARIO_JUMP_ANIMATION_NAME)
				.mirror()
				.build();

		walkAnimation = Animation.builder()
				.spriteSet(resourceManager.loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(2)
				.nFrames(3)
				.frameDuration(100)
				.name(MARIO_WALK_ANIMATION_NAME)
				.mirror()
				.build();

		registeredAnimations.add(walkAnimation);
		registeredAnimations.add(jumpAnimation);
		registeredAnimations.add(idleAnimation);

		setAnimation(jumpAnimation);
	}
	
	@Override
	public void update() {
		super.update();
		tileMap.setPosition(currentPosition.getRoundedX() - (baseWindowSize.getRoundedX() / 2), currentPosition.getRoundedY() - (baseWindowSize.getRoundedY() / 2));
		if (death) {
			GameManager.getInstance().openScene(LostScene.class);
		}

		if (animationJump) {
			setAnimation(jumpAnimation);
		} else if (animationMove && !currentAnimation.getName().equals(MARIO_WALK_ANIMATION_NAME)) {
			setAnimation(walkAnimation);
		} else if (!animationMove) {
			setAnimation(idleAnimation);
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
		if (!(obj instanceof InvisibleBlock) || (obj.currentAnimation.getName().equals(InvisibleBlock.INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME)) || (direction.equals(CollisionDirection.TOP_COLLISION))) {
			super.handleObjectCollisions(direction, obj);
		}
		
		if (obj instanceof Enemy) {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				jumping = true;
			} else {
				death = true;
			}
		} else if (obj instanceof Castle) {
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
			audioManager.playEffect("jump");
		}
	}
 
	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getLeftMovementKeyCode()) {
			left = false;
			if(!right) {
				animationMove = false;
			} else {
				facingRight = true;
			}
		}
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getRightMovementKeyCode()) {
			right = false;
			if(!left) {
				animationMove = false;
			} else {
				facingRight = false;
			}
		}
		if (e.getKeyCode() == GameManager.getInstance().getSettings().getJumpKeyCode()) {
			jumping = false;
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {}
}
