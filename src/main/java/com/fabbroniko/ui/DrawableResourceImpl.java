package com.fabbroniko.ui;

import com.fabbroniko.environment.Position;

import java.awt.image.BufferedImage;

public record DrawableResourceImpl(BufferedImage image, Position position) implements DrawableResource {
}
