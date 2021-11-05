package com.fabbroniko.gameobjects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.*;
import com.fabbroniko.main.GameManager;
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

	public Player(final TileMap tileMap, final GameScene gameScene, final Vector2D position) {
		super(tileMap, gameScene, position, spriteDimension);
		animationJump = true;
		this.gameScene = gameScene;
		this.baseWindowSize = GameManager.getInstance().getCanvasSize();

		idleAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(MARIO_IDLE_ANIMATION_NAME)
				.mirror()
				.build();

		jumpAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.name(MARIO_JUMP_ANIMATION_NAME)
				.mirror()
				.build();

		walkAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
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
		if (isDead()) {
			GameManager.getInstance().openScene(LostScene.class);
		}

		/*
		if (animationJump) {
			setAnimation(jumpAnimation);
		} else if (animationMove && !currentAnimation.getName().equals(MARIO_WALK_ANIMATION_NAME)) {
			setAnimation(walkAnimation);
		} else if (!animationMove) {
			setAnimation(idleAnimation);
		}
		 */
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if(e.getKeyCode() == gameScene.getGameManager().getSettings().getLeftMovementKeyCode()) {
			currentStates.add(State.MOVING_LEFT);
			animationMove = true;
		}

		if(e.getKeyCode() == gameScene.getGameManager().getSettings().getRightMovementKeyCode()) {
			currentStates.add(State.MOVING_RIGHT);
			animationMove = true;
		}

		if(e.getKeyCode() == gameScene.getGameManager().getSettings().getJumpKeyCode()) {
			if(gameScene.getGameManager().getSettings().isFlightMode()) {
				currentStates.add(State.MOVING_UP);
			} else {
				gameScene.getAudioManager().playEffect("jump");
			}

			//animationJump = true;
		}

		if(e.getKeyCode() == KeyEvent.VK_DOWN && gameScene.getGameManager().getSettings().isFlightMode()) {
			if(!currentStates.contains(State.MOVING_UP) && !currentStates.contains(State.MOVING_DOWN)) {
				currentStates.add(State.MOVING_DOWN);
				//animationJump = true;
			}
		}
	}
 
	@Override
	public void keyReleased(final KeyEvent e) {
		if(e.getKeyCode() == gameScene.getGameManager().getSettings().getLeftMovementKeyCode()) {
			currentStates.remove(State.MOVING_LEFT);
			animationMove = false;
		}

		if(e.getKeyCode() == gameScene.getGameManager().getSettings().getRightMovementKeyCode()) {
			currentStates.remove(State.MOVING_RIGHT);
			animationMove = false;
		}

		if(e.getKeyCode() == gameScene.getGameManager().getSettings().getJumpKeyCode()) {
			currentStates.remove(State.MOVING_UP);
		}

		if(e.getKeyCode() == KeyEvent.VK_DOWN && gameScene.getGameManager().getSettings().isFlightMode()) {
			currentStates.remove(State.MOVING_DOWN);
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {}
}
