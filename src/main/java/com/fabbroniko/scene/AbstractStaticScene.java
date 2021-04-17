package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension;
import com.fabbroniko.GameManager;

import java.awt.*;

public abstract class AbstractStaticScene extends AbstractScene {

    private boolean drawn = false;

    protected AbstractStaticScene(final GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void draw(final Graphics2D g, final Dimension d) {
        if(drawn)
            return;

        drawOnce(g, d);

        drawn = true;
    }

    protected abstract void drawOnce(final Graphics2D g, final Dimension d);
}
