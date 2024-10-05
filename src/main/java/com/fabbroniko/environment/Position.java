package com.fabbroniko.environment;

public interface Position {

    void setPosition(final Position model);

    void setPosition(final double x, final double y);

    double getX();

    double getY();

    int getRoundedX();

    int getRoundedY();
}
