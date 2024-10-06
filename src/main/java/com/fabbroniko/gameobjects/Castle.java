package com.fabbroniko.gameobjects;

import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.WinScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Prototype;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

@Prototype
@Component
public class Castle implements GameObject {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(340, 350);
	private static final String spritePath = "/sprites/castle.png";
	private static final String CASTLE_IDLE_ANIMATION_NAME = "CASTLE_IDLE";

	private final TileMap tileMap;
	private final SceneManager sceneManager;
	private final Vector2D mapPosition = new Vector2D();

	private BoundingBox boundingBox;
	private Animation currentAnimation;

	public Castle(final TileMap tileMap,
				  final SceneManager sceneManager,
				  @Qualifier("cachedImageLoader") final ImageLoader imageLoader) {

		this.tileMap = tileMap;
		this.sceneManager = sceneManager;

		setAnimation(Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(CASTLE_IDLE_ANIMATION_NAME)
				.build());
	}

	@Override
	public void update() {
		mapPosition.setPosition(tileMap.getPosition());
	}

	@Override
	public DrawableResource getDrawableResource() {
		final Position position = new ImmutablePosition(boundingBox.position().getRoundedX() - mapPosition.getX(), boundingBox.position().getRoundedY() - mapPosition.getY());
		return new DrawableResourceImpl(currentAnimation.getImage(), position);
	}

	@Override
	public void setInitialPosition(final Position position) {
		this.boundingBox = new BoundingBox(position, spriteDimension);
	}

	@Override
	public void notifyDeath() {
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
	}

	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject gameObject) {
		if (gameObject instanceof Player) {
			sceneManager.openScene(WinScene.class);
		}
	}

	@Override
	public boolean isDead() {
		return false;
	}

	private void setAnimation(final Animation animation) {
		animation.reset();
		this.currentAnimation = animation;
	}
}
