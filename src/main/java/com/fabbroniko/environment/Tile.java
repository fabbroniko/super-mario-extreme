package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

public record Tile(BufferedImage image, TileType type) {
}