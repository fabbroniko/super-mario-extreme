package com.fabbroniko.gameobjects;

import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.ui.DynamicDrawable;

public interface GameObject extends DynamicDrawable {

    void notifyDeath();

    BoundingBox getBoundingBox();

    void handleMapCollisions(final CollisionDirection direction);

    void handleObjectCollisions(final CollisionDirection direction, final GameObject obj);

    boolean isDead();
}
