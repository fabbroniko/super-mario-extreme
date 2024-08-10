package com.fabbroniko.main;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.AudioManagerImpl;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.SceneContextFactoryImpl;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fabbroniko.resource.domain.Settings;
import com.fabbroniko.scene.AbstractScene;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SettingsMenuScene;
import com.fabbroniko.scene.WinScene;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.SneakyThrows;

public final class GameManager {

	private static final GameManager instance = new GameManager();

	private final ResourceManager resourceManager;
	private final AudioManager audioManager;
	private final SceneContextFactory sceneContextFactory;
	@Getter
	private final Settings settings;

	private GamePanel gamePanel;
	private GameThread gameThread;
	private Scene currentState;
	@Getter
	private int deathCount;

	private GameManager() {
		this.resourceManager = new ResourceManager();
		this.audioManager = new AudioManagerImpl(this, resourceManager);
		this.settings = resourceManager.loadSettings();
		this.deathCount = 0;
		this.sceneContextFactory = new SceneContextFactoryImpl(1280, 960);

		this.resourceManager.preload();
	}

	public static GameManager getInstance() {
		return instance;
	}

	@SneakyThrows
	public synchronized void openScene(final Class<? extends AbstractScene> newSceneClass) {
		Scene newScene;

		if (GameScene.class.equals(newSceneClass)) {
			final Level defaultLevel = new XmlMapper().readValue(getClass().getResource("/levels/testing.xml"), Level.class);
			newScene = new GameScene(sceneContextFactory, this, audioManager, resourceManager, defaultLevel);
		} else if (WinScene.class.equals(newSceneClass)) {
			newScene = new WinScene(sceneContextFactory, this, audioManager);
		} else if (LostScene.class.equals(newSceneClass)) {
			deathCount++;
			newScene = new LostScene(sceneContextFactory, this, audioManager);
		} else if (MainMenuScene.class.equals(newSceneClass)) {
			newScene = new MainMenuScene(sceneContextFactory, this, audioManager, resourceManager);
		} else {
			newScene = new SettingsMenuScene(sceneContextFactory, this, audioManager, resourceManager);
		}

		if(currentState != null) {
			currentState.detach();
		}

		this.currentState = newScene;
		this.currentState.init();
	}

	public synchronized void update() {
		if(currentState != null) this.currentState.update();
	}

	public void saveSettings() {
		resourceManager.saveSettings(settings);
	}

	public synchronized void draw(final Graphics2D g, final Vector2D canvasDimension) {
		if(currentState == null) {
			return;
		}

		g.drawImage(this.currentState.draw(), null, 0, 0);
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
