package com.fabbroniko.gamestatemanager;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Background;
import com.fabbroniko.environment.CollisionManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Service;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.GameObjectBuilder;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.resources.Sound;
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
	
	/**
	 * List of {@link AbstractGameObject AbstractGameObject} in this level.
	 */
	protected List<AbstractGameObject> gameObjects;
	
	private final String resBgImage;
	private final String resTileSet;
	private final String resMapFile;
	private CollisionManager collisionManager;
	private GameObjectBuilder gameObjectBuilder;
	
	/**
	 * Constructs a new GenericLevel.
	 * @param bgImage Background image.
	 * @param tileSet TileSet containing the set of Tiles needed for this level.
	 * @param mapFile MapFile containing the matrix of tiles needed to draw the whole map.
	 */
	public AbstractGenericLevel(final String bgImage, final String tileSet, final String mapFile) {
		this.resBgImage = bgImage;
		this.resTileSet = tileSet;
		this.resMapFile = mapFile;
	}
	
	@Override
	public void init() {
		bg = new Background(resBgImage);
		tileMap = new TileMap(resTileSet, resMapFile);
		tileMap.setPosition(Service.ORIGIN.clone());
		gameObjects = new ArrayList<>();
		
		this.collisionManager = new CollisionManager(tileMap, gameObjects);
		gameObjectBuilder = new GameObjectBuilder(tileMap, this);
		
		AudioManager.getInstance().playSound(Sound.getSoundFromName("BackgroundSound"));

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
				gameObjects.remove(i);
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
			GameManager.getInstance().openScene(new MainMenuScene());
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
}