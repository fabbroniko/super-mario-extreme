package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animation;
import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;

public class Castle extends AbstractGameObject {

	private static final Vector2D spriteDimension = new Vector2D(340, 350);
	private static final String spritePath = "/sprites/castle.png";

	public static final String CASTLE_IDLE_ANIMATION_NAME = "CASTLE_IDLE";

	public Castle(final TileMap tileMap,
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
				.name(CASTLE_IDLE_ANIMATION_NAME)
				.build());
	}
}
