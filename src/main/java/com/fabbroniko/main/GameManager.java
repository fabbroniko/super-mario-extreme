package com.fabbroniko.main;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fabbroniko.resource.domain.Settings;
import com.fabbroniko.scene.AbstractScene;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public final class GameManager {

	private static final GameManager instance = new GameManager();

	private final ResourceManager resourceManager;
	private final AudioManager audioManager;

	private final Settings settings;

	private GamePanel gamePanel;
	private GameThread gameThread;
	private AbstractScene currentState;
	private int deathCount;

	private GameManager() {
		this.resourceManager = new ResourceManager();
		this.audioManager = new AudioManager(this, resourceManager);
		this.settings = resourceManager.loadSettings();
		this.deathCount = 0;

		this.resourceManager.preload();
	}

	public static GameManager getInstance() {
		return instance;
	}
	
	/**
	 * Sets the specified state that has to be displayed on the screen.
	 */
	@SneakyThrows
	public synchronized void openScene(final Class<? extends AbstractScene> newSceneClass) {
		AbstractScene newScene;

		if(GameScene.class.equals(newSceneClass)) {
			final Level defaultLevel = new XmlMapper().readValue(getClass().getResource("/levels/lvl1.xml"), Level.class);
			newScene = newSceneClass.getConstructor(GameManager.class, Level.class).newInstance(this, defaultLevel);
		} else {
			newScene = newSceneClass.getConstructor(GameManager.class).newInstance(this);
		}

		if(newScene instanceof LostScene) {
			deathCount++;
		}

		if(currentState != null) {
			currentState.detachScene();
		}

		this.currentState = newScene;
		this.currentState.init();
	}
	
	/**	Updates the image that should be displayed.
	 * 	@see com.fabbroniko.main.Drawable#update()
	 */
	public synchronized void update() {
		if(currentState != null) this.currentState.update();
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public ResourceManager getResourceManager() { return resourceManager; }

	public Settings getSettings() {
		return settings;
	}

	public void saveSettings() {
		resourceManager.saveSettings(settings);
	}

	public synchronized void draw(final Graphics2D g, final Vector2D canvasDimension) {
		if(currentState != null) this.currentState.draw(g, canvasDimension);
	}
	
	public void exit() {
		gameThread.exit();
	}

	public void addKeyListener(final KeyListener keyListener) {
		gamePanel.addKeyListener(keyListener);
	}

	public void removeKeyListener(final KeyListener keyDependent) {
		gamePanel.removeKeyListener(keyDependent);
	}

	public int getDeathCount() {
		return deathCount;
	}

	public void start() {
		gamePanel = new GameWindow().getView();

		gameThread = new GameThread(gamePanel, this);
		gameThread.start();

		openScene(MainMenuScene.class);
	}

	public Vector2D getCanvasSize() {
		return gamePanel.getCanvasSize();
	}
}
