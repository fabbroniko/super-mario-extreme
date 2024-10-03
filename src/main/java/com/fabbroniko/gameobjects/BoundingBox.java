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

    public boolean intersects(final BoundingBox boundingBox) {
        double x2 = this.position.getX() + this.dimension.width();
        double y2 = this.position.getY() + this.dimension.height();

        double otherX2 = boundingBox.position.getX() + boundingBox.dimension.width();
        double otherY2 = boundingBox.position.getY() + boundingBox.dimension.height();

        boolean horizontalOverlap = this.position.getX() < otherX2 && boundingBox.position.getX() < x2;
        boolean verticalOverlap = this.position.getY() < otherY2 && boundingBox.position.getY() < y2;

        return horizontalOverlap && verticalOverlap;
    }
}
