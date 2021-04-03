package fabbroniko.scene;

import fabbroniko.environment.Dimension;

import java.awt.*;

public abstract class AbstractStaticScene extends AbstractScene {

    private boolean drawn = false;

    @Override
    public void draw(final Graphics2D g, final Dimension d) {
        if(drawn)
            return;

        drawOnce(g, d);

        drawn = true;
    }

    protected abstract void drawOnce(final Graphics2D g, final Dimension d);
}
