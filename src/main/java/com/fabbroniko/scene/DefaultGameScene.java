package com.fabbroniko.scene;

import com.fabbroniko.audio.MusicPlayerProvider;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.gameobjects.GameObjectProvider;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.InitializableDrawable;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Component
public final class DefaultGameScene implements GameScene {

    private static final int FPS_OFFSET = 15;

    private DrawableResource background;
    private List<GameObject> gameObjects;

    private final SceneContextFactory sceneContextFactory;
    private final MusicPlayerProvider musicPlayerProvider;
    private final SettingsProvider settingsProvider;
    private final GameObjectProvider gameObjectProvider;
    private final TextFactory textFactory;
    private final BackgroundLoader backgroundLoader;
    private final TileMap tileMap;

    private BufferedImage canvas;
    private Graphics2D graphics;
    private Dimension2D canvasDimension;

    public DefaultGameScene(final GameObjectProvider gameObjectProvider,
                            final SceneContextFactory sceneContextFactory,
                            final SettingsProvider settingsProvider,
                            final MusicPlayerProvider musicPlayerProvider,
                            final TextFactory textFactory,
                            final BackgroundLoader backgroundLoader,
                            final TileMap tileMap) {

        this.gameObjectProvider = gameObjectProvider;
        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.musicPlayerProvider = musicPlayerProvider;
        this.textFactory = textFactory;
        this.backgroundLoader = backgroundLoader;
        this.tileMap = tileMap;
    }

    @Override
    public void init() {
        final SceneContext sceneContext = sceneContextFactory.create();
        this.canvas = sceneContext.canvas();
        this.graphics = (Graphics2D) canvas.getGraphics();
        this.canvasDimension = sceneContext.canvasDimension();

        final InitializableDrawable initializableBackground = backgroundLoader.createStaticBackground("game");
        initializableBackground.init();
        background = initializableBackground.getDrawableResource();
        gameObjects = gameObjectProvider.get();

        musicPlayerProvider.getMusicPlayer().play("theme", true);
    }

    @Override
    public void update() {
        final List<GameObject> deadGameObjects = new ArrayList<>();

        for(final GameObject go : gameObjects) {
            go.update();
            if(go.isDead())
                deadGameObjects.add(go);
        }

        for(final GameObject go : deadGameObjects) {
            gameObjects.remove(go);
        }
    }

    @Override
    public BufferedImage draw() {
        graphics.drawImage(
                background.image(),
                background.position().getRoundedX(),
                background.position().getRoundedY(),
                canvasDimension.width(),
                canvasDimension.height(),
                null
        );

        for (final GameObject i:gameObjects) {
            if (!i.isDead()) {
                final DrawableResource res = i.getDrawableResource();

                graphics.drawImage(res.image(), res.position().getRoundedX(), res.position().getRoundedY(), null);
            }
        }

        final DrawableResource tileMapResource = tileMap.getDrawableResource();
        graphics.drawImage(tileMapResource.image(), tileMapResource.position().getRoundedX(), tileMapResource.position().getRoundedY(), canvasDimension.width(), canvasDimension.height(), null);

        if(settingsProvider.getSettings().isShowFps()) {
            int currentFps = Time.getFps();

            Color color = Color.GREEN;
            if(currentFps < 30) {
                color = Color.RED;
            }

            final BufferedImage fpsImage = textFactory.createParagraph(String.valueOf(currentFps), color);
            final int x = canvasDimension.width() - fpsImage.getWidth() - FPS_OFFSET;
            graphics.drawImage(fpsImage, null, x, 10);
        }

        return canvas;
    }

    @Override
    public void close() {
        musicPlayerProvider.getMusicPlayer().stop();
    }

    @Override
    public List<GameObject> gameObjects() {
        return gameObjects;
    }
}
