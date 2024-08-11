package com.fabbroniko.main;

import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneFactory;
import com.fabbroniko.scene.SettingsMenuScene;
import com.fabbroniko.scene.WinScene;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class GameManager implements Runnable, SceneManager {

    private final SceneFactory sceneFactory;
	private final ResourceManager resourceManager;
	private final SettingsProvider settingsProvider;
    private final GamePanel gamePanel;

	private volatile boolean running = false;
	private Scene currentState;

	public GameManager(final GamePanel gamePanel,
					   final ResourceManager resourceManager,
                       final SettingsProvider settingsProvider,
                       final SceneFactory sceneFactory) {

        this.settingsProvider = settingsProvider;
		this.resourceManager = resourceManager;
		this.sceneFactory = sceneFactory;
        this.gamePanel = gamePanel;
	}

	public void openMainMenu() {
		openScene(sceneFactory.createMainMenuScene(this));
	}

	public void openSettings() {
		openScene(sceneFactory.createSettingsScene());
	}

	@Override
	public void openWinScene() {
		openScene(sceneFactory.createWinScene());
	}

	@Override
	public void openLostScene() {
		openScene(sceneFactory.createLostScene());
	}

	@Override
	public void openGameScene() {
		openScene(sceneFactory.createGameScene(this));
	}

	@SneakyThrows
	private synchronized void openScene(Scene scene) {
		if(currentState != null) {
			currentState.detach();
		}

		this.currentState = scene;
		this.gamePanel.setKeyListener(scene);
		this.currentState.init();
	}

	public synchronized void update() {
		if(currentState == null) {
			return;
		}

		this.currentState.update();
		if(currentState.isClosed()) {
			if (currentState.getClass().equals(MainMenuScene.class)) {
				exit();
			} else if (currentState.getClass().equals(SettingsMenuScene.class)) {
				openMainMenu();
			} else if (currentState.getClass().equals(LostScene.class)) {
				openGameScene();
			} else if (currentState.getClass().equals(WinScene.class)) {
				openMainMenu();
			} else if (currentState.getClass().equals(GameScene.class)) {
				openMainMenu();
			}
		}
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
