package com.fabbroniko;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.GameWindow;
import com.fabbroniko.main.IView;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fabbroniko.scene.AbstractScene;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

/**
 * Handles the current state of the game (e.g. Menus, Levels, etc.)
 * @author nicola.fabbrini
 *
 */
public final class GameManager implements Drawable {

	private static final GameManager instance = new GameManager();

	private final Object synchronize;
	private final ResourceManager resourceManager;
	private final AudioManager audioManager;

	private final Settings settings;

	private static IView view;
	private AbstractScene currentState;
	private int deathCount;

	private GameManager() {
		this.synchronize = new Object();
		this.resourceManager = new ResourceManager();
		this.audioManager = new AudioManager(this, resourceManager);
		this.settings = new Settings();
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

	public void draw(final Graphics2D g, final Dimension gDimension) {
		synchronized (synchronize) {
			if(currentState != null) this.currentState.draw(g, gDimension);
		}
	}
	
	public void exit() {
		view.exit();
	}
	
	public Dimension getBaseWindowSize() {
		return view.getBaseWindowSize();
	}

	public void addKeyListener(final KeyListener keyListener) {
		view.addKeyListener(keyListener);
	}

	public void removeKeyListener(final KeyListener keyDependent) {
		view.removeKeyListener(keyDependent);
	}

	public int getDeathCount() {
		return deathCount;
	}

	public void start() {
		view = new GameWindow().getView();

		openScene(MainMenuScene.class);
	}
}