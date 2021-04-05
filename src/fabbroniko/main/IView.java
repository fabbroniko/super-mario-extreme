package fabbroniko.main;

import fabbroniko.environment.Dimension;

import java.awt.event.KeyListener;

public interface IView {

	boolean isRunning();
	
	void exit();
	
	Dimension getBaseWindowSize();

	void addKeyListener(final KeyListener keyListener);

	void removeKeyListener(final KeyListener keyListener);
}
