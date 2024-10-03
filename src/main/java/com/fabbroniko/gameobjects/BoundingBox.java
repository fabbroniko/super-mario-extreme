package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Position;

public class BoundingBox {

    private final Position position;
    private final Dimension2D dimension;

    public BoundingBox(final Position position, final Dimension2D dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    public Position position() {
        return position;
    }

    public Dimension2D dimension() {
        return dimension;
    }
}
