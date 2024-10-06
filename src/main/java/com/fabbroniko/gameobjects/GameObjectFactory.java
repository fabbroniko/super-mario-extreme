package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Position;

public interface GameObjectFactory {

    GameObject create(final String name, final Position initialPosition);
}
