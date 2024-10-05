package com.fabbroniko.main;

import com.fabbroniko.scene.NullScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.context.ApplicationContext;
import lombok.SneakyThrows;

@Component
public final class GameManager implements SceneManager, CycleListener {

    private final ApplicationContext applicationContext;
    private final GameRenderer gameRenderer;
	private final GameCycle gameCycle;
	private Scene currentState = new NullScene();

	public GameManager(final ApplicationContext applicationContext,
					   final GameRenderer gameRenderer,
                       final GameCycle gameCycle) {

		this.applicationContext = applicationContext;
        this.gameRenderer = gameRenderer;
        this.gameCycle = gameCycle;
    }

	@Override
	public void quit() {
		gameCycle.stop();
	}

	@Override
	@SneakyThrows
	public synchronized void openScene(final Class<? extends Scene> scene) {
		this.currentState.close();
		this.currentState = applicationContext.getInstance(scene);
		this.currentState.init();
	}

	public void start() {
		gameRenderer.init();

		openScene(MainMenuScene.class);

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
