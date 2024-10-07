package com.fabbroniko.environment;

public record BoundingBox(Position position, Dimension2D dimension) {

    public boolean intersects(final BoundingBox boundingBox) {
        double x2 = this.position.getX() + this.dimension.width();
        double y2 = this.position.getY() + this.dimension.height();

        double otherX2 = boundingBox.position.getX() + boundingBox.dimension.width();
        double otherY2 = boundingBox.position.getY() + boundingBox.dimension.height();

        boolean horizontalOverlap = this.position.getX() < otherX2 && boundingBox.position.getX() < x2;
        boolean verticalOverlap = this.position.getY() < otherY2 && boundingBox.position.getY() < y2;

        return horizontalOverlap && verticalOverlap;
    }

    public boolean within(final BoundingBox boundingBox) {
        final Position targetPosition = boundingBox.position;
        final Dimension2D targetDimension = boundingBox.dimension;

        return
            (position.getX() + dimension.width()) > targetPosition.getX() &&
            (position.getY() + dimension.height()) > targetPosition.getY() &&
            position.getX() < (targetPosition.getX() + targetDimension.width()) &&
            position.getY() < (targetPosition.getY() + targetDimension.height());
    }
}
