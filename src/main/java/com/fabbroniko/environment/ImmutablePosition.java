package com.fabbroniko.environment;

public class ImmutablePosition implements Position {

    private final int x;
    private final int y;

    public ImmutablePosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public ImmutablePosition(final double x, final double y) {
        this.x = (int)Math.round(x);
        this.y = (int)Math.round(y);
    }

    @Override
    public void setPosition(final Position model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPosition(double x, double y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public int getRoundedX() {
        return x;
    }

    @Override
    public int getRoundedY() {
        return y;
    }
}
