package com.fabbroniko.environment;

import java.awt.Graphics2D;
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
	public void draw(final Graphics2D g, final Dimension gDimension) {
		g.drawImage(backgroundImage, origin.getX(), origin.getY(), gDimension.getWidth(), gDimension.getHeight(), null);
	}	
}
