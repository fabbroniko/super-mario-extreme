package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
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

    private final EffectPlayerProvider effectPlayerProvider;
    private final ImageLoader imageLoader;
    private final SettingsProvider settingsProvider;

    public GameObjectFactoryImpl(final EffectPlayerProvider effectPlayerProvider,
                                 @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                                 final SettingsProvider settingsProvider) {
        this.effectPlayerProvider = effectPlayerProvider;
        this.imageLoader = imageLoader;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public Player createPlayer(final Dimension2D canvasDimension, final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Player(tileMap, canvasDimension, settingsProvider, gameScene, imageLoader, effectPlayerProvider, position);
    }

    @Override
    public GameObject createCastle(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Castle(tileMap, gameScene, imageLoader, effectPlayerProvider, position);
    }

    @Override
    public GameObject createEnemy(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Enemy(tileMap, gameScene, imageLoader, effectPlayerProvider, position);
    }

    @Override
    public GameObject createBlock(final GameScene gameScene, final Vector2D position, final TileMap tileMap) {
        return new Block(tileMap, imageLoader, effectPlayerProvider, position);
    }
}
