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
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Prototype;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

@Prototype
@Component
public class Enemy implements AnimationListener, GameObject {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(108, 192);
	private static final String spritePath = "/sprites/ghost.png";
	private static final String ENEMY_WALK_ANIMATION_NAME = "ENEMY_WALK";
	private static final String ENEMY_DEAD_ANIMATION_NAME = "ENEMY_DEAD";
	private boolean init;
	private final Animation deadAnimation;
	protected BoundingBox boundingBox;

	protected Vector2D mapPosition = new Vector2D();
	protected Animation currentAnimation;
	private final TileMap tileMap;
	private final EffectPlayerProvider effectPlayerProvider;
	private final CollisionManager collisionManager;

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

	public Enemy(final TileMap tileMap,
                 @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                 final EffectPlayerProvider effectPlayerProvider,
				 final CollisionManager collisionManager) {

		this.tileMap = tileMap;
		this.effectPlayerProvider = effectPlayerProvider;
        this.collisionManager = collisionManager;

        falling = true;
		walkingSpeed = 300;

		deadAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.frameDuration(200)
				.animationListener(this)
				.name(ENEMY_DEAD_ANIMATION_NAME)
				.mirror()
				.build();

		setAnimation(Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(2)
				.frameDuration(100)
				.name(ENEMY_WALK_ANIMATION_NAME)
				.mirror()
				.build());
	}

	@Override
	public void animationFinished() {
		death = true;
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
			collisionManager.calculateMovement(this, offset);
			boundingBox.position().setPosition(boundingBox.position().getX() + offset.getX(), boundingBox.position().getY() + offset.getY());
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

		if (direction.equals(CollisionDirection.BOTTOM_COLLISION) && !init) {
			right = true;
			facingRight = true;
			init = true;
		}
		if (direction.equals(CollisionDirection.LEFT_COLLISION)) {
			left = false;
			facingRight = false;
			right = true;
		}
		if (direction.equals(CollisionDirection.RIGHT_COLLISION)) {
			right = false;
			facingRight = true;
			left = true;
		}
	}

	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject collidedGameObject) {
		handleMapCollisions(direction);

		if (direction.equals(CollisionDirection.TOP_COLLISION) && collidedGameObject instanceof Player && !currentAnimation.getName().equals(ENEMY_DEAD_ANIMATION_NAME)) {
			this.setAnimation(deadAnimation);
			effectPlayerProvider.getEffectPlayer().play("hit");
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
