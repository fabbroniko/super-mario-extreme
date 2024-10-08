package com.fabbroniko.environment;

import com.fabbroniko.sdi.annotation.Component;

@Component
public class CanvasSize implements Dimension2D {

    private static final int CANVAS_WIDTH = 1280;
    private static final int CANVAS_HEIGHT = 960;

    @Override
    public int width() {
        return CANVAS_WIDTH;
    }

    @Override
    public int height() {
        return CANVAS_HEIGHT;
    }
}
