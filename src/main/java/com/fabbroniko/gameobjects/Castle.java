package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayer;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;

public class Castle extends AbstractGameObject {

	private static final Vector2D spriteDimension = new Vector2D(340, 350);
	private static final String spritePath = "/sprites/castle.png";

	public static final String CASTLE_IDLE_ANIMATION_NAME = "CASTLE_IDLE";

	public Castle(final TileMap tileMap,
				  final GameScene gameScene,
				  final ResourceManager resourceManager,
				  final EffectPlayer effectPlayer,
				  final Vector2D position) {
		super(tileMap, gameScene, resourceManager, effectPlayer, position, spriteDimension);

		setAnimation(Animation.builder()
				.spriteSet(resourceManager.loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(1)
				.name(CASTLE_IDLE_ANIMATION_NAME)
				.build());
	}
}
