package fabbroniko.gamestatemanager;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fabbroniko.environment.Dimension;
import fabbroniko.main.IView;

/**
 * Handles the current state of the game (e.g. Menus, Levels, etc.)
 * @author nicola.fabbrini
 *
 */
public final class GameStateManager implements IGameStateManager, KeyListener {
	
	private AbstractGameState currentState;
	private final Object synchronize;
	private static IView view;
	
	private static GameStateManager myInstance;
	
	/**
	 * Constructs a new GameStateManager
	 */
	private GameStateManager() {
		synchronize = new Object();
	}
	
	/**
	 * Gets the single instance of this class.
	 * @return The single instance of this class.
	 */
	public static GameStateManager getInstance() {
		if(myInstance == null) {
			myInstance = new GameStateManager();
		}
		return myInstance;
	}
	
	public static GameStateManager setInstance(final IView viewParam) {
		view = viewParam;
		return getInstance();
	}
	
	/**
	 * Sets the specified state that has to be displayed on the screen.
	 * @param selectedState State that has to be draw on the screen
	 */
	public void setState(final State selectedState) {
		synchronized (synchronize) {
			this.currentState = selectedState.getGameState();
			currentState.init();
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
	
	/**	Draws the current state.
	 * 	@param g Graphic Context
	 * 	@see fabbroniko.main.Drawable#draw(Graphics2D)
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
