package com.fabbroniko.main;

import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public interface DrawableResource {

    BufferedImage image();

    Position position();
}
