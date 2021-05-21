package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.scene.GameScene;

import java.awt.image.BufferedImage;
import java.util.List;

public class InvisibleBlock extends AbstractGameObject {

	private static final Dimension spriteDimension = new Dimension(30, 30);
	private static final String spritePath = "/sprites/invisible-block.png";

	public static final String INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME = "INV_BLK_VISIBLE";
	public static final String INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME = "INV_BLK_INVISIBLE";

	private final Animation visibleAnimation;

	public InvisibleBlock(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, objectID, spriteDimension);

		List<BufferedImage> frames = generateSprites(spritePath, spriteDimension, 0, 1);
		final Animation invisibleAnimation = new Animation(INVISIBLE_BLOCK_INVISIBLE_ANIMATION_NAME, frames, 1, true, null);
		setAnimation(invisibleAnimation);

		frames = generateSprites(spritePath, spriteDimension, 1, 1);
		visibleAnimation = new Animation(INVISIBLE_BLOCK_VISIBLE_ANIMATION_NAME, frames, 1, true, null);
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
