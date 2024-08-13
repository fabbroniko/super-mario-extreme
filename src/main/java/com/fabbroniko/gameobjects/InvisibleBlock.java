package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.AudioManager;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.*;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;

public class InvisibleBlock extends AbstractGameObject {

	private static final Vector2D spriteDimension = new Vector2D(120, 120);
	private static final String spritePath = "/sprites/invisible-block.png";

	public static final String INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME = "INV_BLK_VISIBLE";
	public static final String INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME = "INV_BLK_INVISIBLE";

	private final Animation visibleAnimation;

	public InvisibleBlock(final TileMap tileMap,
						  final GameScene gameScene,
						  final ResourceManager resourceManager,
						  final AudioManager audioManager,
						  final Vector2D position) {
		super(tileMap, gameScene, resourceManager, audioManager, position, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(resourceManager.loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME)
				.build());

		visibleAnimation = Animation.builder()
				.spriteSet(resourceManager.loadImageFromDisk(spritePath))
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
			audioManager.playEffect("hit");
		}
	}
}
