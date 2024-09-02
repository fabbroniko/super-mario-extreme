package com.fabbroniko.main;

import com.fabbroniko.input.ControlsSource;

import java.awt.image.BufferedImage;

public interface GameRenderer extends ControlsSource {

	void init();

	void draw(final BufferedImage frame);

	void close();
}
