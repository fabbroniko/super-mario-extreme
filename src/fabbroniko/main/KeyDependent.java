package fabbroniko.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Represents a class that can be controlled through the keyboard.
 * @author fabbroniko
 */
public interface KeyDependent {

	/** 
	 * A key has been pressed.
	 * @param e Contains details about the event. 
	 * @see KeyListener#keyPressed(KeyEvent)
	 */
	void keyPressed(final KeyEvent e);
	
	/**
	 * A key has been released.
	 * @param e Contains details about the event. 
	 * @see KeyListener#keyReleased(KeyEvent)
	 */
	void keyReleased(final KeyEvent e);
}
