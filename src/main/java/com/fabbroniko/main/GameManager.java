package com.fabbroniko.main;

import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.scene.NullScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.factory.SceneFactory;
import lombok.SneakyThrows;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class GameManager implements Runnable, SceneManager {

    private final SceneFactory sceneFactory;
	private final SettingsProvider settingsProvider;
    private final GamePanel gamePanel;

	private volatile boolean running = false;
	private Scene currentState = new NullScene();
	private int deathCount = 0;

	public GameManager(final GamePanel gamePanel,
					   final SettingsProvider settingsProvider,
					   final SceneFactory sceneFactory) {

        this.settingsProvider = settingsProvider;
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
		this.currentState.close();
		this.currentState = scene;
		this.gamePanel.setKeyListener(scene);
		this.currentState.init();
	}

	public synchronized void update() {
		this.currentState.update();
	}

	public synchronized BufferedImage draw() {
		return this.currentState.draw();
	}

	public void start() {
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
