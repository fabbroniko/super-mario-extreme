package com.fabbroniko.ui.background;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.ui.InitializableDrawable;

import java.awt.Color;

public interface BackgroundLoader {

    InitializableDrawable createStaticBackground(final String resourceName);

    InitializableDrawable createSimpleColorBackground(final Color color, final Dimension2D canvasDimension);
}
