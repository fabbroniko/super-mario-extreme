package com.fabbroniko.scene;

import java.awt.image.BufferedImage;

public class NullScene implements Scene {

    @Override
    public void init() {
    }

    @Override
    public void update() {
    }

    @Override
    public BufferedImage draw() {
        return null;
    }

    @Override
    public void close() {
    }
}
