package com.fabbroniko.ui.background;

import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.ui.InitializableDrawable;
import lombok.Getter;

import java.awt.image.BufferedImage;

public class StaticBackground implements InitializableDrawable {

	private final BufferedImage image;
	private final DrawableResourceFactory drawableResourceFactory;

	@Getter
	private DrawableResource drawableResource;

	public StaticBackground(final BufferedImage image,
							final DrawableResourceFactory drawableResourceFactory) {

		this.image = image;
		this.drawableResourceFactory = drawableResourceFactory;
	}

	@Override
	public void init() {
		this.drawableResource = drawableResourceFactory.createDrawableResource(image);
	}
}
