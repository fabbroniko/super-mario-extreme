package com.fabbroniko.ui.background;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.ui.InitializableDrawable;
import lombok.Getter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SimpleColorBackground implements InitializableDrawable {

	private final Color color;
	private final Dimension2D canvasDimension;
	private final DrawableResourceFactory drawableResourceFactory;

	@Getter
	private DrawableResource drawableResource;

	public SimpleColorBackground(final Color color,
								 final Dimension2D canvasDimension,
                                 final DrawableResourceFactory drawableResourceFactory) {

		this.color = color;
		this.canvasDimension = canvasDimension;
		this.drawableResourceFactory = drawableResourceFactory;
	}

	@Override
	public void init() {
		final BufferedImage background = new BufferedImage(canvasDimension.width(), canvasDimension.height(), BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics = background.createGraphics();
		graphics.setColor(color);
		graphics.drawRect(0, 0, canvasDimension.width(), canvasDimension.height());

		this.drawableResource = drawableResourceFactory.createDrawableResource(background);
	}
}
