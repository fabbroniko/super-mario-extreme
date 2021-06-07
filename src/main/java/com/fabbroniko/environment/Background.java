package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;

public class Background implements Drawable {

	private final BufferedImage backgroundImage;
	private final Position origin = new Position();

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
	public Position getDrawingPosition() {
		return origin.clone();
	}
}
