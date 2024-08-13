package com.fabbroniko.ui.background;

import com.fabbroniko.ui.Drawable;

public interface BackgroundLoader {

    Drawable createStaticBackground(final String resourceName);
}
