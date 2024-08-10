package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

public class SceneContext {

    private final BufferedImage canvas;
    private final Dimension2D canvasDimension;

    public SceneContext(BufferedImage canvas, Dimension2D canvasDimension) {
        this.canvas = canvas;
        this.canvasDimension = canvasDimension;
    }

    public BufferedImage getSceneCanvas() {
        return canvas;
    }

    public Dimension2D getCanvasDimension() {
        return canvasDimension;
    }
}
