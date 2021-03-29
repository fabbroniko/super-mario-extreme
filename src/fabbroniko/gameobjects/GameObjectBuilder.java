package fabbroniko.gameobjects;

import java.lang.reflect.InvocationTargetException;

import fabbroniko.environment.Position;
import fabbroniko.environment.TileMap;
import fabbroniko.gamestatemanager.AbstractGenericLevel;

/**
 * Service Class used to constructs {@link AbstractGameObject AbstractGameObject}.
 * @author fabbroniko
 */
public class GameObjectBuilder {

	private AbstractGameObject currentObject;
	private final TileMap tileMap;
	private final AbstractGenericLevel genericLevel;
	
	private int id;
	
	/**
	 * Constructs a new GameObjectBuilder.
	 * @param tileMapP The {@link TileMap TileMap} passed to the {@link AbstractGameObject AbstractGameObject}.
	 * @param genericLevelP The Level where the AbstractGameObject is going to be placed on.
	 */
	public GameObjectBuilder(final TileMap tileMapP, final AbstractGenericLevel genericLevelP) {
		this.tileMap = tileMapP;
		this.genericLevel = genericLevelP;
		id = 0;
	} 
	
	/**
	 * Creates a new {@link AbstractGameObject AbstractGameObject}.
	 * @param objectClass Class of the AbstractGameObject that has to be created.
	 * @return This instance of the GameObjectBuilder.
	 */
	public GameObjectBuilder newInstance(final Class<? extends AbstractGameObject> objectClass) {
		try {
			currentObject = objectClass.getConstructor(TileMap.class, AbstractGenericLevel.class, Integer.class).newInstance(tileMap, genericLevel, id);
			id++;
		} catch (InstantiationException e) {
			throw new fabbroniko.error.InstantiationException(objectClass);
		} catch (IllegalAccessException e2) {
			throw new fabbroniko.error.InstantiationException(objectClass);
		} catch (IllegalArgumentException e3) {
			throw new fabbroniko.error.InstantiationException(objectClass);
		} catch (InvocationTargetException e4) {
			throw new fabbroniko.error.InstantiationException(objectClass);
		} catch (NoSuchMethodException e5) {
			throw new fabbroniko.error.InstantiationException(objectClass);
		} catch (SecurityException e6) {
			throw new fabbroniko.error.InstantiationException(objectClass);
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
