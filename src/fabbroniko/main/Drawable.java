package fabbroniko.main;

import java.awt.Graphics2D;

import fabbroniko.environment.Dimension;

public interface Drawable {

	void update();

	void draw(final Graphics2D g, final Dimension gDimension);
}
