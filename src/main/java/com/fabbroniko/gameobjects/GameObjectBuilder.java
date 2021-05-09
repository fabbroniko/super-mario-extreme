package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.scene.GameScene;

/**
 * Service Class used to constructs {@link AbstractGameObject AbstractGameObject}.
 * @author com.fabbroniko
 */
public class GameObjectBuilder {

	private AbstractGameObject currentObject;
	private final TileMap tileMap;
	private final GameScene gameScene;
	
	private int id;

	public GameObjectBuilder(final TileMap tileMapP, final GameScene gameScene) {
		this.tileMap = tileMapP;
		this.gameScene = gameScene;
		id = 0;
	} 

	public GameObjectBuilder newInstance(final Class<? extends AbstractGameObject> objectClass) {
		try {
			currentObject = objectClass.getConstructor(TileMap.class, GameScene.class, Integer.class).newInstance(tileMap, gameScene, id);
			id++;
		} catch (final Exception e) {
			throw new com.fabbroniko.error.InstantiationException(objectClass);
		}

		return this;
	}
	
	/**
	 * Sets the position of the current AbstractGameObject.
	 * @param position AbstractGameObject's Position.
	 * @return This instance of the GameObjectBuilder.
	 */
	public GameObjectBuilder setPosition(final Position position) {
		currentObject.setObjectPosition(position);
		return this;
	}
	
	/**
	 * Gets the instance of the current {@link AbstractGameObject AbstractGameObject}. 
	 * @return Returns the instance of the current AbstractGameObject
	 */
	public AbstractGameObject getInstance() {
		return this.currentObject;
	}
}
