package com.fabbroniko.main;

import com.fabbroniko.audio.AudioManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.Block;
import com.fabbroniko.gameobjects.Castle;
import com.fabbroniko.gameobjects.Enemy;
import com.fabbroniko.gameobjects.FallingBlock;
import com.fabbroniko.gameobjects.InvisibleBlock;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.GameScene;

public class GameObjectFactoryImpl implements GameObjectFactory {

    private final AudioManager audioManager;
    private final ResourceManager resourceManager;
    private final SettingsProvider settingsProvider;
    private final SceneManager sceneManager;

    public GameObjectFactoryImpl(AudioManager audioManager, ResourceManager resourceManager, SettingsProvider settingsProvider, SceneManager sceneManager) {
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
        this.settingsProvider = settingsProvider;
        this.sceneManager = sceneManager;
    }

    @Override
    public Player createPlayer(final Dimension2D canvasDimension, final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Player(tileMap, canvasDimension, settingsProvider, gameScene, resourceManager, audioManager, position, sceneManager);
    }

    @Override
    public AbstractGameObject createCastle(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Castle(tileMap, gameScene, resourceManager, audioManager, position);
    }

    @Override
    public AbstractGameObject createEnemy(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Enemy(tileMap, gameScene, resourceManager, audioManager, position);
    }

    @Override
    public AbstractGameObject createInvisibleBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new InvisibleBlock(tileMap, gameScene, resourceManager, audioManager, position);
    }

    @Override
    public AbstractGameObject createBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Block(tileMap, gameScene, resourceManager, audioManager, position);
    }

    @Override
    public AbstractGameObject createFallingBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new FallingBlock(tileMap, gameScene, resourceManager, audioManager, position);
    }
}
