package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.scene.GameScene;

import java.awt.image.BufferedImage;
import java.util.List;

public class FallingBlock extends AbstractGameObject {

	private static final Dimension spriteDimension = new Dimension(90, 30);
	private static final String spritePath = "/sprites/falling-block.png";

	public static final String FALLING_BLOCK_IDLE_ANIMATION_NAME = "FALL_BLK_IDLE";

	public FallingBlock(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, objectID, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(FALLING_BLOCK_IDLE_ANIMATION_NAME)
				.build());
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);
		
		if (obj instanceof Player && direction.equals(CollisionDirection.TOP_COLLISION)) {
			falling = true;
		}
	}
}
