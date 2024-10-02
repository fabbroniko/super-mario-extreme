package com.fabbroniko.scene;

import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.collision.CollisionManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.LevelProvider;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.input.TypedLessKeyListener;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.resource.dto.LevelDto;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.InitializableDrawable;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.VK_ESCAPE;

@Component
public final class GameScene implements Scene, TypedLessKeyListener {

    private static final int FPS_OFFSET = 15;

    private final LevelProvider levelProvider;
    private DrawableResource background;
    private TileMap tileMap;
    private List<AbstractGameObject> gameObjects;
    private CollisionManager collisionManager;
    private Player player;

    private final SceneContextFactory sceneContextFactory;
    private final MusicPlayer musicPlayer;
    private final ImageLoader imageLoader;
    private final SettingsProvider settingsProvider;
    private final SceneManager sceneManager;
    private final GameObjectFactory gameObjectFactory;
    private final TextFactory textFactory;
    private final BackgroundLoader backgroundLoader;

    private BufferedImage canvas;
    private Graphics2D graphics;
    private Dimension2D canvasDimension;

    public GameScene(final SceneContextFactory sceneContextFactory,
                     final SettingsProvider settingsProvider,
                     final MusicPlayer musicPlayer,
                     @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                     final SceneManager sceneManager,
                     final TextFactory textFactory,
                     final BackgroundLoader backgroundLoader,
                     final GameObjectFactory gameObjectFactory,
                     final LevelProvider levelProvider) {

        this.gameObjectFactory = gameObjectFactory;
        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.musicPlayer = musicPlayer;
        this.imageLoader = imageLoader;
        this.sceneManager = sceneManager;
        this.textFactory = textFactory;
        this.backgroundLoader = backgroundLoader;
        this.levelProvider = levelProvider;
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
        tileMap = new TileMap(imageLoader, level.getMap(), canvasDimension);
        gameObjects = new ArrayList<>();

        this.collisionManager = new CollisionManager(tileMap, gameObjects);

        player = gameObjectFactory.createPlayer(canvasDimension, this, level.getStartPosition(), tileMap);
        this.addNewObject(player);

        level.getGameObjects().forEach(gameObject -> this.addNewObject(
                createGameObject(gameObject.getType(),
                new Vector2D(gameObject.getX(), gameObject.getY())))
        );

        musicPlayer.play("theme", true);
    }

    private AbstractGameObject createGameObject(final String name, final Vector2D startPosition) {
        return switch (name) {
            case "castle" -> gameObjectFactory.createCastle(this, startPosition, tileMap);
            case "invisible-block" -> gameObjectFactory.createInvisibleBlock(this, startPosition, tileMap);
            case "falling-platform" -> gameObjectFactory.createFallingBlock(this, startPosition, tileMap);
            case "ghost-enemy" -> gameObjectFactory.createEnemy(this, startPosition, tileMap);
            case "breakable-block" -> gameObjectFactory.createBlock(this, startPosition, tileMap);
            default ->
                    throw new IllegalArgumentException("The specified game object with name " + name + " doesn't exist.");
        };
    }

    public void checkForCollisions(final AbstractGameObject obj, final Vector2D offsetPosition) {
        this.collisionManager.checkForCollisions(obj, offsetPosition);
    }

    private void addNewObject(final AbstractGameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void levelFinished() {
       sceneManager.openScene(WinScene.class);
    }

    @Override
    public void update() {
        if(player.isDead()) {
            sceneManager.openScene(LostScene.class);
        }

        final List<AbstractGameObject> deadGameObjects = new ArrayList<>();

        for(final AbstractGameObject go : gameObjects) {
            go.update();
            if(go.isDead())
                deadGameObjects.add(go);
        }

        for(final AbstractGameObject go : deadGameObjects) {
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

        for (final AbstractGameObject i:gameObjects) {
            if (!i.isDead()) {
                final DrawableResource res = i.getDrawableResource();
                final Vector2D spriteDimension = i.getSpriteDimension();

                graphics.drawImage(res.image(), res.position().getRoundedX(), res.position().getRoundedY(), spriteDimension.getRoundedX(), spriteDimension.getRoundedY(), null);
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
        musicPlayer.stop();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(player != null) {
            player.keyPressed(event);
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if(player != null) {
            player.keyReleased(event);
        }

        if (VK_ESCAPE == event.getKeyCode()) {
            sceneManager.openScene(MainMenuScene.class);
        }
    }
}
