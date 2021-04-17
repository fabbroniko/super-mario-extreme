package com.fabbroniko.scene;

import com.fabbroniko.environment.Position;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.Block;
import com.fabbroniko.gameobjects.Castle;
import com.fabbroniko.gameobjects.Enemy;
import com.fabbroniko.gameobjects.FallingBlock;
import com.fabbroniko.gameobjects.InvisibleBlock;
import com.fabbroniko.gamestatemanager.AbstractGenericLevel;
import com.fabbroniko.gamestatemanager.GameManager;
import com.fabbroniko.resources.domain.Level;

/**
 * First Level.
 * @author com.fabbroniko
 */
public final class GameScene extends AbstractGenericLevel {

	public GameScene(final GameManager gameManager, final Level level) {
		super(gameManager, level);
	}

	@Override
	public void init() {
		super.init();

		level.getGameObjects().forEach(gameObject -> this.addNewObject(
				getClassFromName(gameObject.getType()),
				new Position(gameObject.getX(), gameObject.getY()))
		);
	}

	private Class<? extends AbstractGameObject> getClassFromName(final String name) {
		switch (name) {
			case "castle":
				return Castle.class;
			case "invisible-block":
				return InvisibleBlock.class;
			case "falling-platform":
				return FallingBlock.class;
			case "ghost-enemy":
				return Enemy.class;
			case "breakable-block":
				return Block.class;
			default:
				throw new IllegalArgumentException("The specified game object with name " + name + " doesn't exist.");
		}
	}

	@Override
	protected Position getPreferredStartPosition() {
		return level.getStartPosition().clone();
	}
	
	@Override
	public void levelFinished() {
		gameManager.openScene(WinScene.class);
	}
}
