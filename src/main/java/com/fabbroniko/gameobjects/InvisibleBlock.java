package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.scene.GameScene;

public class InvisibleBlock extends AbstractGameObject {

	private static final Dimension spriteDimension = new Dimension(120, 120);
	private static final String spritePath = "/sprites/invisible-block.png";

	public static final String INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME = "INV_BLK_VISIBLE";
	public static final String INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME = "INV_BLK_INVISIBLE";

	private final Animation visibleAnimation;

	public InvisibleBlock(final TileMap tileMap, final GameScene gameScene, final Position position) {
		super(tileMap, gameScene, position, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME)
				.build());

		visibleAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
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
			this.gameScene.getAudioManager().playEffect("hit");
		}
	}
}
