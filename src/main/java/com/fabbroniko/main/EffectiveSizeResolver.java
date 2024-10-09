package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

import java.awt.Dimension;
import java.awt.Toolkit;

@Component
public class EffectiveSizeResolver implements WindowSizeResolver {

    private final Dimension2D canvasSize;

    public EffectiveSizeResolver(@Qualifier("canvasSize") final Dimension2D canvasSize) {
        this.canvasSize = canvasSize;
    }

    @Override
    public Dimension dimension() {
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        final double widthMultiplier = dimension.getWidth() / canvasSize.width();
        final double heightMultiplier = dimension.getHeight() / canvasSize.height();
        final double multiplier = Math.min(widthMultiplier, heightMultiplier);

        final int width = (int) Math.floor(canvasSize.width() * multiplier);
        final int height = (int) Math.floor(canvasSize.height() * multiplier);

        return new Dimension(width, height);
    }
}
