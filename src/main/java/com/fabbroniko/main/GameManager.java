package com.fabbroniko.main;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneFactory;
import lombok.SneakyThrows;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class GameManager implements Runnable, SceneManager {

	private final AudioManager audioManager;
    private final SceneFactory sceneFactory;
	private final ResourceManager resourceManager;
	private final SettingsProvider settingsProvider;
    private final GamePanel gamePanel;

	private volatile boolean running = false;
	private Scene currentState;
	private int deathCount = 0;

	public GameManager(final AudioManager audioManager,
					   final GamePanel gamePanel,
					   final ResourceManager resourceManager,
					   final SettingsProvider settingsProvider,
					   final SceneFactory sceneFactory) {

		this.audioManager = audioManager;
        this.settingsProvider = settingsProvider;
		this.resourceManager = resourceManager;
		this.sceneFactory = sceneFactory;
        this.gamePanel = gamePanel;
	}

	public void openMainMenu() {
		openScene(sceneFactory.createMainMenuScene(this));
	}

	public void openSettings() {
		openScene(sceneFactory.createSettingsScene(this));
	}

	@Override
	public void openWinScene() {
		openScene(sceneFactory.createWinScene(this));
	}

	@Override
	public void openLostScene() {
		openScene(sceneFactory.createLostScene(this, ++deathCount));
	}

	@Override
	public void openGameScene() {
		openScene(sceneFactory.createGameScene(this));
	}

	@Override
	public void quit() {
		exit();
	}

	@SneakyThrows
	private synchronized void openScene(final Scene scene) {
		audioManager.stopMusic();
		this.currentState = scene;
		this.gamePanel.setKeyListener(scene);
		this.currentState.init();
	}

	public synchronized void update() {
		if(currentState == null) {
			return;
		}

		this.currentState.update();
	}

	public synchronized BufferedImage draw() {
		if(currentState == null) {
			return null;
		}

		return this.currentState.draw();
	}

	public void start() {
		resourceManager.preload();
		openMainMenu();

		new Thread(this).start();
	}

	@SneakyThrows
	@Override
	public void run() {
		final Graphics2D canvas = gamePanel.getCanvas();
		running = true;

		// Game Loop
		while (running) {
			this.update();

			final BufferedImage frame = this.draw();
			if(frame != null) {
				canvas.drawImage(this.draw(), null, 0, 0);

				gamePanel.repaint();
			}

			Time.sync(settingsProvider.getSettings().getFpsCap());
		}

		System.exit(0);
	}

	public void exit() {
		running = false;
	}
}
