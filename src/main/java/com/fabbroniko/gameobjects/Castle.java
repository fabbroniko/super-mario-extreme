package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animation;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.scene.GameScene;

public class Castle extends AbstractGameObject {

	private static final Dimension spriteDimension = new Dimension(340, 350);
	private static final String spritePath = "/sprites/castle.png";

	public static final String CASTLE_IDLE_ANIMATION_NAME = "CASTLE_IDLE";

	public Castle(final TileMap tileMap, final GameScene gameScene, final Position position) {
		super(tileMap, gameScene, position, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(CASTLE_IDLE_ANIMATION_NAME)
				.build());
	}
}
