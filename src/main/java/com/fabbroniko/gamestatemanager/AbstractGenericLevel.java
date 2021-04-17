package com.fabbroniko.gamestatemanager;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import com.fabbroniko.environment.*;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.GameObjectBuilder;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.resources.domain.Level;
import com.fabbroniko.scene.AbstractScene;
import com.fabbroniko.scene.MainMenuScene;

/**
 * Represents a generic level.
 * @author com.fabbroniko
 *
 */
public abstract class AbstractGenericLevel extends AbstractScene implements KeyListener {
	
	private Background bg;
	
	/**
	 * Reference of the {@link TileMap TileMap}.
	 */
	protected TileMap tileMap;

	protected List<AbstractGameObject> gameObjects;
	protected final Level level;

	private CollisionManager collisionManager;
	private GameObjectBuilder gameObjectBuilder;

	public AbstractGenericLevel(final GameManager gameManager, final Level level) {
		super(gameManager);

		this.level = level;
	}
	
	@Override
	public void init() {
		bg = new Background(gameManager.getResourceManager(), "game");
		tileMap = new TileMap(gameManager.getResourceManager(), level.getMap());
		tileMap.setPosition(new Position());
		gameObjects = new ArrayList<>();
		
		this.collisionManager = new CollisionManager(tileMap, gameObjects);
		gameObjectBuilder = new GameObjectBuilder(tileMap, this);
		
		audioManager.playBackgroundMusic("theme", true);

		final Player player = (Player) this.addNewObject(Player.class, getPreferredStartPosition());

		gameManager.addKeyListener(player);
		gameManager.addKeyListener(this);
	}

	@Override
	public void detachScene() {
		super.detachScene();

		gameManager.removeKeyListener(this);
	}
	
	@Override
	public void update() {
		super.update();
		
		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).update();
			if (gameObjects.get(i).isDead()) {
				gameObjects.remove(i); // TODO refactor
			}
		}
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		bg.draw(g, gDimension);
		
		for (final AbstractGameObject i:gameObjects) {
			if (!i.isDead()) {
				i.draw(g, gDimension);	
			}
		}
		
		tileMap.draw(g, gDimension);
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameManager.getInstance().openScene(MainMenuScene.class);
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {}

	@Override
	public void keyTyped(final KeyEvent e) {}

	public final void checkForCollisions(final AbstractGameObject obj, final Position offsetPosition) {
		this.collisionManager.checkForCollisions(obj, offsetPosition);
	}
	
	/**
	 * Adds a new {@link AbstractGameObject AbstractGameObject} in this level.
	 */
	protected AbstractGameObject addNewObject(final Class<? extends AbstractGameObject> objectClass, final Position position) {
		final AbstractGameObject newGameObject = gameObjectBuilder.newInstance(objectClass).setPosition(position).getInstance();
		gameObjects.add(newGameObject);

		return newGameObject;
	}

	/**
	 * Gets the list of {@link AbstractGameObject AbstractGameObject} placed in this level.
	 * @return The list of
	 */
	public List<AbstractGameObject> getGameObjects() {
		return this.gameObjects;
	}

	/**
	 * Gets the preferred starting position for the player.
	 * @return The preferred starting position.
	 */
	protected abstract Position getPreferredStartPosition();

	/**
	 * Loads the next level or the win window.
	 */
	public abstract void levelFinished();

	// TODO tmp to refactor
	public AudioManager getAudioManager() {
		return audioManager;
	}
}
