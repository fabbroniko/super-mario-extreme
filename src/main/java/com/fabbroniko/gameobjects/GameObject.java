package com.fabbroniko.gameobjects;

import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.ui.DynamicDrawable;

import java.awt.Rectangle;

public interface GameObject extends DynamicDrawable {

    void notifyDeath();

    Vector2D getPosition();

    Vector2D getDimension();

    void handleMapCollisions(final CollisionDirection direction);

    void handleObjectCollisions(final CollisionDirection direction, final GameObject obj);

    Rectangle getRectangle();

    boolean isDead();
}
