package fabbroniko.gamestatemanager;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Dimension;
import fabbroniko.main.Drawable;
import fabbroniko.main.IView;
import fabbroniko.scene.AbstractScene;

/**
 * Handles the current state of the game (e.g. Menus, Levels, etc.)
 * @author nicola.fabbrini
 *
 */
public final class GameManager implements Drawable, KeyListener {

	private static GameManager myInstance;

	private final Object synchronize;
	private final AudioManager audioManager;

	private static IView view;
	private AbstractScene currentState;
	
	/**
	 * Constructs a new GameStateManager
	 */
	private GameManager() {
		synchronize = new Object();
		audioManager = AudioManager.getInstance();
	}
	
	/**
	 * Gets the single instance of this class.
	 * @return The single instance of this class.
	 */
	public static GameManager getInstance() {
		if(myInstance == null) {
			myInstance = new GameManager();
		}
		return myInstance;
	}
	
	public static GameManager setInstance(final IView viewParam) {
		view = viewParam;
		return getInstance();
	}
	
	/**
	 * Sets the specified state that has to be displayed on the screen.
	 */
	public void openScene(final AbstractScene abstractScene) {
		abstractScene.attachGameManager(this);

		synchronized (synchronize) {
			if(currentState != null) {
				currentState.detachScene();
			}

			this.currentState = abstractScene;
			this.currentState.init();
		}
	}
	
	/**	Updates the image that should be displayed.
	 * 	@see fabbroniko.main.Drawable#update()
	 */
	public void update() {
		synchronized (synchronize) {
			this.currentState.update();
		}
	}

	public AudioManager getAudioManager() {
		return this.audioManager;
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

	@Override
	public void keyPressed(final KeyEvent e) {
		this.currentState.keyPressed(e);
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		this.currentState.keyReleased(e);
	}
	
	@Override
	public void keyTyped(final KeyEvent e) {}
}
