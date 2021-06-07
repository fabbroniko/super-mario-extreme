package com.fabbroniko.main;

import java.awt.image.BufferedImage;

import com.fabbroniko.environment.Position;

public interface Drawable {

	void update();

	BufferedImage getDrawableImage();

	Position getDrawingPosition();
}
