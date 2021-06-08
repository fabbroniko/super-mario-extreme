package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;

public class Background implements Drawable {

	private final BufferedImage backgroundImage;
	private final Vector2D origin = new Vector2D();

	public Background(final ResourceManager resourceManager, final String backgroundName) {
		backgroundImage = resourceManager.findBackgroundFromName(backgroundName);
	}

	@Override
	public void update() {}

	@Override
	public BufferedImage getDrawableImage() {
		return backgroundImage;
	}

	@Override
	public Vector2D getDrawingPosition() {
		return origin.clone();
	}
}
