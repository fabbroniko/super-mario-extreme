package com.fabbroniko.environment;

public interface Position {

    void setVector2D(final Vector2D model);

    void setVector2D(final double x, final double y);

    double getX();

    double getY();

    int getRoundedX();

    int getRoundedY();
}
