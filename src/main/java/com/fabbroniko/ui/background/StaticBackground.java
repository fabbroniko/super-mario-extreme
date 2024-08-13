package com.fabbroniko.ui.background;

import com.fabbroniko.environment.Position;
import com.fabbroniko.ui.Drawable;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceFactory;
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
