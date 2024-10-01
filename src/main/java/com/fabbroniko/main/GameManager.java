package com.fabbroniko.main;

import com.fabbroniko.scene.NullScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.factory.SceneFactory;
import com.fabbroniko.sdi.annotation.Component;
import lombok.SneakyThrows;

@Component
public final class GameManager implements SceneManager, CycleListener {

    private final SceneFactory sceneFactory;
    private final GameRenderer gameRenderer;
	private final GameCycle gameCycle;
	private final CustomKeyListener customKeyListener;
	private Scene currentState = new NullScene();

	public GameManager(final GameRenderer gameRenderer,
                       final SceneFactory sceneFactory,
                       final GameCycle gameCycle,
					   final CustomKeyListener customKeyListener) {

		this.sceneFactory = sceneFactory;
        this.gameRenderer = gameRenderer;
        this.gameCycle = gameCycle;
        this.customKeyListener = customKeyListener;
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
		openScene(sceneFactory.createLostScene(this));
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
		this.customKeyListener.setKeyListener(scene);
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
