package com.fabbroniko.collision;

import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.GameObject;

public interface CollisionManager {

	Position calculateMovement(final GameObject gameObject, final Position offsetPosition);
}
