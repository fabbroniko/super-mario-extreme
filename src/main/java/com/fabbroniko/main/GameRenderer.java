package com.fabbroniko.main;

import java.awt.image.BufferedImage;

public interface GameRenderer {

	void init();

	void draw(final BufferedImage frame);

	void close();
}
