package com.fabbroniko.main;

import com.fabbroniko.input.ControlsSource;

import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public interface GameCanvas extends ControlsSource, KeyListener {

	void init();

	void draw(final BufferedImage frame);

	void close();
}
