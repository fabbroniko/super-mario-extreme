package com.fabbroniko.main;

import com.fabbroniko.scene.NullScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.factory.SceneFactory;
import lombok.SneakyThrows;

public final class GameManager implements SceneManager, CycleListener {

    private final SceneFactory sceneFactory;
    private final GameRenderer gameRenderer;
	private final GameCycle gameCycle;
	private Scene currentState = new NullScene();
	private int deathCount = 0;

	public GameManager(final GameRenderer gameRenderer,
                       final SceneFactory sceneFactory,
					   final GameCycle gameCycle) {

		this.sceneFactory = sceneFactory;
        this.gameRenderer = gameRenderer;
        this.gameCycle = gameCycle;
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
		gameCycle.stop();
	}

	@SneakyThrows
	private synchronized void openScene(final Scene scene) {
		this.currentState.close();
		this.currentState = scene;
		this.gameRenderer.setKeyListener(scene);
		this.currentState.init();
	}

	public void start() {
		gameRenderer.init();

		openMainMenu();

		gameCycle.run(this);
	}

	@Override
	public void init() {
		gameRenderer.init();
	}

	public synchronized void update() {
		this.currentState.update();
	}

	public synchronized void draw() {
		this.gameRenderer.draw(currentState.draw());
	}

	@Override
	public void close() {
		gameRenderer.close();
	}
}
