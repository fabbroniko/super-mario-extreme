package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animation;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.scene.GameScene;

import java.awt.image.BufferedImage;
import java.util.List;

public class Castle extends AbstractGameObject {

	private static final Dimension spriteDimension = new Dimension(170, 175);
	private static final String spritePath = "/sprites/castle.png";

	public static final String CASTLE_IDLE_ANIMATION_NAME = "CASTLE_IDLE";

	public Castle(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, objectID, spriteDimension);

		final List<BufferedImage> frames = generateSprites(spritePath, spriteDimension, 0, 1);
		final Animation idleAnimation = new Animation(CASTLE_IDLE_ANIMATION_NAME, frames, 1, true, null);
		setAnimation(idleAnimation);
	}
}
