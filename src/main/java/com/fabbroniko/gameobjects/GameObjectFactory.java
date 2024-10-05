package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Position;

public interface GameObjectFactory {

    Player createPlayer(final Position initialPosition);

    GameObject createCastle(final Position initialPosition);

    GameObject createEnemy(final Position initialPosition);

    GameObject createBlock(final Position initialPosition);
}
