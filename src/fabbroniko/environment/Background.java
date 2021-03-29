package fabbroniko.environment;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import fabbroniko.main.Drawable;

/**
 * Represents a background that can be placed in a GameState.
 * @author nicola.fabbrini
 *
 */
public class Background implements Drawable {

	private BufferedImage bgImg;
	private Position bgPosition;
	
	/**
	 * Constructs a new Background.
	 * @param bg Background Path.
	 */
	public Background(final String bg) {
		bgImg = Service.getImageFromName(bg);
		bgPosition = Service.ORIGIN.clone();
	}

	@Override
	public void update() {}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		g.drawImage(bgImg, bgPosition.getX(), bgPosition.getY(), gDimension.getWidth(), gDimension.getHeight(), null);
	}	
}
