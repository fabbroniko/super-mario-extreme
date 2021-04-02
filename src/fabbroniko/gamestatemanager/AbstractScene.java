package fabbroniko.gamestatemanager;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fabbroniko.environment.Dimension;
import fabbroniko.main.Drawable;
import fabbroniko.main.KeyDependent;

/**
 * Represents a generic state.
 * @author nicola.fabbrini
 */
public abstract class AbstractScene implements Drawable, KeyDependent {
	
	/**
	 * Initializes the current gameState.
	 */
	public abstract void init();
	
	@Override
	public void update() {}
	
	@Override
	public abstract void draw(final Graphics2D g, final Dimension gDimension);
	
	@Override
	public void keyPressed(final KeyEvent e) {}
	
	@Override
	public void keyReleased(final KeyEvent e) {}
}
