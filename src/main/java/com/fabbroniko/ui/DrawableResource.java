package com.fabbroniko.ui;

import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public interface DrawableResource {

    BufferedImage image();

    Position position();
}
