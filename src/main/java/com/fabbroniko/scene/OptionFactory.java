package com.fabbroniko.scene;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface OptionFactory {

    BufferedImage getMainMenuOption(final String text, final Color color);
}
