package com.fabbroniko.scene;

import com.fabbroniko.audio.MusicPlayerProvider;
import com.fabbroniko.collision.CollisionManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.LevelProvider;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.GameObject;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.dto.LevelDto;
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
import java.util.stream.Collectors;

@Component
public final class GameScene implements Scene {

    private static final int FPS_OFFSET = 15;

    private DrawableResource background;
    private List<GameObject> gameObjects;

    private final SceneContextFactory sceneContextFactory;
    private final MusicPlayerProvider musicPlayerProvider;
    private final SettingsProvider settingsProvider;
    private final GameObjectFactory gameObjectFactory;
    private final TextFactory textFactory;
    private final BackgroundLoader backgroundLoader;
    private final LevelProvider levelProvider;
    private final TileMap tileMap;
    private final CollisionManager collisionManager;

    private BufferedImage canvas;
    private Graphics2D graphics;
    private Dimension2D canvasDimension;

    public GameScene(final SceneContextFactory sceneContextFactory,
                     final SettingsProvider settingsProvider,
                     final MusicPlayerProvider musicPlayerProvider,
                     final TextFactory textFactory,
                     final BackgroundLoader backgroundLoader,
                     final GameObjectFactory gameObjectFactory,
                     final LevelProvider levelProvider,
                     final TileMap tileMap,
                     final CollisionManager collisionManager) {

        this.gameObjectFactory = gameObjectFactory;
        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.musicPlayerProvider = musicPlayerProvider;
        this.textFactory = textFactory;
        this.backgroundLoader = backgroundLoader;
        this.levelProvider = levelProvider;
        this.tileMap = tileMap;
        this.collisionManager = collisionManager;
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

        final LevelDto level = levelProvider.getLevel();
        gameObjects = level.getGameObjects()
            .stream()
            .map(gameObject -> gameObjectFactory.create(gameObject.getType(), new Vector2D(gameObject.getX(), gameObject.getY())))
            .collect(Collectors.toCollection(ArrayList::new));

        musicPlayerProvider.getMusicPlayer().play("theme", true);
    }

    public void checkForCollisions(final GameObject gameObject, final Vector2D offsetPosition) {
        this.collisionManager.checkForCollisions(gameObject, offsetPosition, gameObjects);
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
}
