package fabbroniko.main;

import fabbroniko.environment.Dimension;

public interface IView {

	boolean isRunning();
	
	void exit();
	
	Dimension getBaseWindowSize();
}
