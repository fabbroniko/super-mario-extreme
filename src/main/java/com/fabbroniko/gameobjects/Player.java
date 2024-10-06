package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.collision.CollisionManager;
import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.input.TypedLessKeyListener;
import com.fabbroniko.main.CustomKeyListener;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Prototype;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.VK_ESCAPE;

@Prototype
@Component
public class Player implements TypedLessKeyListener, GameObject {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(112, 104);
	private static final String spritePath = "/sprites/mario.png";

	public static final String MARIO_IDLE_ANIMATION_NAME = "MARIO_IDLE";
	public static final String MARIO_WALK_ANIMATION_NAME = "MARIO_WALK";
	public static final String MARIO_JUMP_ANIMATION_NAME = "MARIO_JUMP";

	private boolean animationJump;
	private boolean animationMove;
	
	private final Dimension2D baseWindowSize;
	
	private final Animation walkAnimation;
	private final Animation idleAnimation;
	private final Animation jumpAnimation;

	private final SettingsProvider settingsProvider;
	protected BoundingBox boundingBox;

	protected Vector2D mapPosition = new Vector2D();
	protected Animation currentAnimation;
	protected List<Animation> registeredAnimations = new ArrayList<>();
	private final TileMap tileMap;
	private final CollisionManager collisionManager;
	private final SceneManager sceneManager;
	private final EffectPlayerProvider effectPlayerProvider;

    protected boolean jumping;
	protected boolean falling;
	protected boolean left;
	protected boolean right;
	protected boolean facingRight;
	protected boolean groundHit;
	protected int currentJump;
	protected boolean death = false;
	protected int jumpSpeed = -1000;
	protected int gravitySpeed = 600;
	protected int walkingSpeed = 600;
	protected int maxJump = 400;

	protected Vector2D offset = new Vector2D();

	public Player(final TileMap tileMap,
                  @Qualifier("canvasSize") final Dimension2D baseWindowSize,
                  final SettingsProvider settingsProvider,
                  final CollisionManager collisionManager,
                  @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                  final SceneManager sceneManager,
                  final EffectPlayerProvider effectPlayerProvider,
				  final CustomKeyListener customKeyListener) {

		this.tileMap = tileMap;
		this.collisionManager = collisionManager;
        this.sceneManager = sceneManager;
        this.effectPlayerProvider = effectPlayerProvider;

		this.settingsProvider = settingsProvider;

        falling = true;
		animationJump = true;
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

		customKeyListener.setKeyListener(this);
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
		if (VK_ESCAPE == e.getKeyCode()) {
			sceneManager.openScene(MainMenuScene.class);
		}

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

	@Override
	public void update() {
		double xOffset = 0;
		double yOffset = 0;

		mapPosition.setPosition(tileMap.getPosition());

		if (jumping) {
			yOffset += (jumpSpeed * Time.deltaTime());
			currentJump += yOffset;
			if (currentJump < -maxJump) {
				jumping = false;
			}
		}

		yOffset += falling && !jumping ? (gravitySpeed * Time.deltaTime()) : 0;
		xOffset += left ? (-walkingSpeed * Time.deltaTime()) : 0;
		xOffset += right ? (walkingSpeed * Time.deltaTime()) : 0;

		if (xOffset != 0 || yOffset != 0) {
			offset.setX(xOffset);
			offset.setY(yOffset);
			collisionManager.checkForCollisions(this, offset);
			boundingBox.position().setPosition(boundingBox.position().getX() + offset.getX(), boundingBox.position().getY() + offset.getY());
		}

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
	public DrawableResource getDrawableResource() {
		final Position position = new ImmutablePosition(boundingBox.position().getRoundedX() - mapPosition.getX(), boundingBox.position().getRoundedY() - mapPosition.getY());
		if(facingRight) {
			return new DrawableResourceImpl(currentAnimation.getImage(), position);
		} else {
			return new DrawableResourceImpl(currentAnimation.getMirroredImage(), position);
		}
	}

	@Override
	public void setInitialPosition(final Position position) {
		this.boundingBox = new BoundingBox(position, spriteDimension);
	}

	@Override
	public void notifyDeath() {
		this.death = true;
		sceneManager.openScene(LostScene.class);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
		if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			groundHit = true;
			offset.setY(0);
		}
		if (direction.equals(CollisionDirection.TOP_COLLISION)) {
			jumping = false;
			offset.setY(0);
		}
		if (direction.equals(CollisionDirection.LEFT_COLLISION) || direction.equals(CollisionDirection.RIGHT_COLLISION)) {
			offset.setX(0);
		}

		if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			animationJump = false;
		}
	}

	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject gameObject) {
		if ((direction.equals(CollisionDirection.TOP_COLLISION))) {
			handleMapCollisions(direction);
		}

		if (gameObject instanceof Enemy) {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				jumping = true;
			} else {
				death = true;
			}
		} else {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				animationJump = false;
				groundHit = true;
			}
		}
	}

	@Override
	public boolean isDead() {
		return death;
	}

	private void setAnimation(final Animation animation) {
		animation.reset();
		this.currentAnimation = animation;
	}
}
