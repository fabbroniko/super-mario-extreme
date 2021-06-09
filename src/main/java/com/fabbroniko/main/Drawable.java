package com.fabbroniko.main;

import java.awt.image.BufferedImage;

import com.fabbroniko.environment.Vector2D;

public interface Drawable {

	void update();

	BufferedImage getDrawableImage();

	Vector2D getDrawingPosition();
}
