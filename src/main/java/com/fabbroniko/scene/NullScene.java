package com.fabbroniko.scene;

import com.fabbroniko.input.ActionLessKeyListener;

import java.awt.image.BufferedImage;

public class NullScene implements Scene, ActionLessKeyListener {

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
