package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayer;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;

@Component
public class GameObjectFactoryImpl implements GameObjectFactory {

    private final EffectPlayer effectPlayer;
    private final ImageLoader imageLoader;
    private final SettingsProvider settingsProvider;

    public GameObjectFactoryImpl(final EffectPlayer effectPlayer,
                                 @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                                 final SettingsProvider settingsProvider) {
        this.effectPlayer = effectPlayer;
        this.imageLoader = imageLoader;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public Player createPlayer(final Dimension2D canvasDimension, final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Player(tileMap, canvasDimension, settingsProvider, gameScene, imageLoader, effectPlayer, position);
    }

    @Override
    public AbstractGameObject createCastle(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Castle(tileMap, gameScene, imageLoader, effectPlayer, position);
    }

    @Override
    public AbstractGameObject createEnemy(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Enemy(tileMap, gameScene, imageLoader, effectPlayer, position);
    }

    @Override
    public AbstractGameObject createInvisibleBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new InvisibleBlock(tileMap, gameScene, imageLoader, effectPlayer, position);
    }

    @Override
    public AbstractGameObject createBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Block(tileMap, gameScene, imageLoader, effectPlayer, position);
    }

    @Override
    public AbstractGameObject createFallingBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new FallingBlock(tileMap, gameScene, imageLoader, effectPlayer, position);
    }
}
