package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.input.TypedLessKeyListener;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.settings.SettingsProvider;
import lombok.extern.log4j.Log4j2;

import java.awt.event.KeyEvent;

/**
 * Represents the player's character.
 * @author com.fabbroniko
 */
@Log4j2
public class Player extends AbstractGameObject implements TypedLessKeyListener {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(112, 104);
	private static final String spritePath = "/sprites/mario.png";

	public static final String MARIO_IDLE_ANIMATION_NAME = "MARIO_IDLE";
	public static final String MARIO_WALK_ANIMATION_NAME = "MARIO_WALK";
	public static final String MARIO_JUMP_ANIMATION_NAME = "MARIO_JUMP";

	private boolean animationJump;
	private boolean animationMove;

	private final GameScene gameScene;
	
	private final Dimension2D baseWindowSize;
	
	private final Animation walkAnimation;
	private final Animation idleAnimation;
	private final Animation jumpAnimation;

	private final SettingsProvider settingsProvider;

	public Player(final TileMap tileMap,
				  final Dimension2D baseWindowSize,
				  final SettingsProvider settingsProvider,
				  final GameScene gameScene,
				  final ImageLoader imageLoader,
				  final EffectPlayerProvider effectPlayerProvider,
				  final Vector2D position) {
		super(tileMap, gameScene, imageLoader, effectPlayerProvider, position, spriteDimension);

		this.settingsProvider = settingsProvider;

		falling = true;
		animationJump = true;
		this.gameScene = gameScene;
		this.baseWindowSize = baseWindowSize;

		idleAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(MARIO_IDLE_ANIMATION_NAME)
				.mirror()
				.build();

		jumpAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.name(MARIO_JUMP_ANIMATION_NAME)
				.mirror()
				.build();

		walkAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
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
		tileMap.setPosition(boundingBox.position().getRoundedX() - (baseWindowSize.width() / 2), boundingBox.position().getRoundedY() - (baseWindowSize.height() / 2));

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
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject gameObject) {
		if (!(gameObject instanceof InvisibleBlock) || (direction.equals(CollisionDirection.TOP_COLLISION))) {
			super.handleObjectCollisions(direction, gameObject);
		}
		
		if (gameObject instanceof Enemy) {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				jumping = true;
			} else {
				death = true;
			}
		} else if (gameObject instanceof Castle) {
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
		if (e.getKeyCode() == settingsProvider.getSettings().getLeftMovementKeyCode()) {
			left = true;
			animationMove = true;
			facingRight = false;
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getRightMovementKeyCode()) {
			right = true;
			animationMove = true;
			facingRight = true;
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getJumpKeyCode() && !jumping && groundHit) {
			jumping = true;
			groundHit = false;
			currentJump = 0;
			animationJump = true;
			effectPlayerProvider.getEffectPlayer().play("jump");
		}
	}
 
	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.getKeyCode() == settingsProvider.getSettings().getLeftMovementKeyCode()) {
			left = false;
			if(!right) {
				animationMove = false;
			} else {
				facingRight = true;
			}
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getRightMovementKeyCode()) {
			right = false;
			if(!left) {
				animationMove = false;
			} else {
				facingRight = false;
			}
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getJumpKeyCode()) {
			jumping = false;
		}
	}
}
