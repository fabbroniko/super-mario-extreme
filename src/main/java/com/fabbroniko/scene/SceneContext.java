package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;

import java.awt.image.BufferedImage;

public record SceneContext(BufferedImage canvas, Dimension2D canvasDimension) {
}
