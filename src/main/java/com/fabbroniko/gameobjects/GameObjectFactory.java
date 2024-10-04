package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.scene.GameScene;

public interface GameObjectFactory {

    Player createPlayer(final Dimension2D canvasDimension, final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    GameObject createCastle(final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    GameObject createEnemy(final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    GameObject createBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap);
}
