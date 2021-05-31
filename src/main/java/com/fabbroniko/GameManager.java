package com.fabbroniko;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.GamePanel;
import com.fabbroniko.main.GameWindow;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fabbroniko.scene.AbstractScene;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public final class GameManager implements Drawable {

	private static final GameManager instance = new GameManager();

	private final Object synchronize;
	private final ResourceManager resourceManager;
	private final AudioManager audioManager;

	private final Settings settings;

	private GamePanel gamePanel;
	private GameThread gameThread;
	private AbstractScene currentState;
	private int deathCount;

	private GameManager() {
		this.synchronize = new Object();
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
	public void openScene(final Class<? extends AbstractScene> newSceneClass) {
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

		synchronized (synchronize) {
			if(currentState != null) {
				currentState.detachScene();
			}

			this.currentState = newScene;
			this.currentState.init();
		}
	}
	
	/**	Updates the image that should be displayed.
	 * 	@see com.fabbroniko.main.Drawable#update()
	 */
	public void update() {
		synchronized (synchronize) {
			if(currentState != null) this.currentState.update();
		}
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

	public void draw(final Graphics2D g, final Dimension gDimension) {
		synchronized (synchronize) {
			if(currentState != null) this.currentState.draw(g, gDimension);
		}
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

		gameThread = new GameThread();
		gameThread.start();

		openScene(MainMenuScene.class);
	}

	public Dimension getCanvasSize() {
		return gamePanel.getCanvasSize();
	}

	private class GameThread extends Thread {

		private final AtomicBoolean isRunning = new AtomicBoolean(false);

		@SneakyThrows
		@Override
		public void run() {
			long currentTime;
			long wait;

			final Dimension canvasDimension = getCanvasSize();
			final Graphics2D canvas = gamePanel.getCanvas();
			isRunning.set(true);

			// Game Loop
			while (isRunning.get()) {

				currentTime = System.currentTimeMillis();

				update();
				draw(canvas, canvasDimension);
				gamePanel.repaint();

				wait = GameWindow.FPS_MILLIS - (System.currentTimeMillis() - currentTime);
				if (wait < 0) {
					wait = 0;
				}

				Thread.sleep(wait);
			}

			System.exit(0);
		}

		public void exit() {
			isRunning.set(false);
		}
	}
}
