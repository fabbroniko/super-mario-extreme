package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayer;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;

public class InvisibleBlock extends AbstractGameObject {

	private static final Vector2D spriteDimension = new Vector2D(120, 120);
	private static final String spritePath = "/sprites/invisible-block.png";

	public static final String INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME = "INV_BLK_VISIBLE";
	public static final String INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME = "INV_BLK_INVISIBLE";

	private final Animation visibleAnimation;

	public InvisibleBlock(final TileMap tileMap,
						  final GameScene gameScene,
						  final ImageLoader imageLoader,
						  final EffectPlayer effectPlayer,
						  final Vector2D position) {
		super(tileMap, gameScene, imageLoader, effectPlayer, position, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME)
				.build());

		visibleAnimation = Animation.builder()
				.spriteSet(imageLoader.findSpritesByName(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.name(INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME)
				.build();
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj)
	{
		if (obj instanceof Player && direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			this.setAnimation(visibleAnimation);
			effectPlayer.play("hit");
		}
	}
}
