package com.fabbroniko.map;

import java.awt.image.BufferedImage;

public record Tile(BufferedImage image, TileType type) {
}