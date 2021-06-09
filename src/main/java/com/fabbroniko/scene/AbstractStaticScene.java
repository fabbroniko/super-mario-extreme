package com.fabbroniko.scene;

import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.GameManager;

import java.awt.Graphics2D;

public abstract class AbstractStaticScene extends AbstractScene {

    private boolean drawn = false;

    protected AbstractStaticScene(final GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void draw(final Graphics2D g, final Vector2D d) {
        if(drawn)
            return;

        drawOnce(g, d);

        drawn = true;
    }

    protected abstract void drawOnce(final Graphics2D g, final Vector2D d);
}
