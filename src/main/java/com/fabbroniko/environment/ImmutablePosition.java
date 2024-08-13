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
    public int getRoundedX() {
        return x;
    }

    @Override
    public int getRoundedY() {
        return y;
    }
}
