package com.fabbroniko.collision;

import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.GameObject;

public interface CollisionManager {

	void calculateMovement(final GameObject gameObject, final Vector2D offsetPosition);
}
