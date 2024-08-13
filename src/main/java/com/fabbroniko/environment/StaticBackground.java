package com.fabbroniko.environment;

import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.DrawableResource;
import com.fabbroniko.main.DrawableResourceFactory;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public class StaticBackground implements Drawable {

	private final DrawableResource drawableResource;

	public StaticBackground(final BufferedImage image,
							final Position position,
							final DrawableResourceFactory drawableResourceFactory) {

		this.drawableResource = drawableResourceFactory.createDrawableResource(image, position);
	}
}
