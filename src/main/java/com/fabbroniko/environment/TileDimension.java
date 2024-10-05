package com.fabbroniko.environment;

import com.fabbroniko.sdi.annotation.Component;

@Component("tileDimension")
public class TileDimension implements Dimension2D {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 120;

    @Override
    public int width() {
        return WIDTH;
    }

    @Override
    public int height() {
        return HEIGHT;
    }
}
