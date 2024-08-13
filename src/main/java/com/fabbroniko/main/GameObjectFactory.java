package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.scene.GameScene;

public interface GameObjectFactory {

    Player createPlayer(final Dimension2D canvasDimension, final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    AbstractGameObject createCastle(final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    AbstractGameObject createEnemy(final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    AbstractGameObject createInvisibleBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    AbstractGameObject createBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap);

    AbstractGameObject createFallingBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap);
}
