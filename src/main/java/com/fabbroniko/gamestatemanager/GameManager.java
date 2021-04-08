package com.fabbroniko.gamestatemanager;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import com.fabbroniko.Settings;
import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.IView;
import com.fabbroniko.resources.ResourceManager;
import com.fabbroniko.scene.AbstractScene;

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

	private GameManager() {
		this.synchronize = new Object();
		this.resourceManager = new ResourceManager();
		this.audioManager = new AudioManager(this, resourceManager);
		this.settings = new Settings();

		resourceManager.preload();
	}

	public static GameManager getInstance() {
		return instance;
	}

	// TODO refactor
	public static GameManager setInstance(final IView viewParam) {
		view = viewParam;
		return getInstance();
	}
	
	/**
	 * Sets the specified state that has to be displayed on the screen.
	 */
	public void openScene(final AbstractScene newScene) {
		newScene.attachGameManager(this);

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
			this.currentState.update();
		}
	}

	public AudioManager getAudioManager() {
		return this.audioManager;
	}

	public Settings getSettings() {
		return settings;
	}

	/**	Draws the current state.
	 * 	@param g Graphic Context
	 */
	public void draw(final Graphics2D g, final Dimension gDimension) {
		synchronized (synchronize) {
			this.currentState.draw(g, gDimension);
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
}
