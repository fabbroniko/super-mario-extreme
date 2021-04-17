package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Animations;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.scene.GameScene;

public class Castle extends AbstractGameObject {

	public Castle(final TileMap tileMap, final GameScene gameScene, final Integer objectID) {
		super(tileMap, gameScene, Animations.CASTLE, objectID);
	}
}
