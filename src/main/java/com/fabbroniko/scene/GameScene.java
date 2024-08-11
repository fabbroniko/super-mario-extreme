package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Background;
import com.fabbroniko.environment.CollisionManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.TileMap;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.main.GameObjectFactory;
import com.fabbroniko.main.GameObjectFactoryImpl;
import com.fabbroniko.main.SceneManager;
import com.fabbroniko.main.SettingsProvider;
import com.fabbroniko.main.Time;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.VK_ESCAPE;

public final class GameScene extends AbstractScene implements Scene {

    private static final int FPS_OFFSET = 15;

    private final Level level;
    private Background bg;
    private TileMap tileMap;
    private List<AbstractGameObject> gameObjects;
    private CollisionManager collisionManager;
    private boolean isClosed = false;
    private Player player;

    private final SceneContextFactory sceneContextFactory;
    private final AudioManager audioManager;
    private final ResourceManager resourceManager;
    private final SettingsProvider settingsProvider;
    private final SceneManager sceneManager;
    private final GameObjectFactory gameObjectFactory;

    private BufferedImage canvas;
    private Graphics2D graphics;
    private Dimension2D canvasDimension;

    public GameScene(final SceneContextFactory sceneContextFactory,
                     final SettingsProvider settingsProvider,
                     final AudioManager audioManager,
                     final ResourceManager resourceManager,
                     final SceneManager sceneManager,
                     final Level level) {

        this.gameObjectFactory = new GameObjectFactoryImpl(audioManager, resourceManager, settingsProvider, sceneManager);
        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
        this.sceneManager = sceneManager;
        this.level = level;
    }

    @Override
    public void init() {
        final SceneContext sceneContext = sceneContextFactory.create();
        this.canvas = sceneContext.getSceneCanvas();
        this.graphics = (Graphics2D) canvas.getGraphics();
        this.canvasDimension = sceneContext.getCanvasDimension();

        bg = new Background(resourceManager, "game");
        tileMap = new TileMap(resourceManager, level.getMap(), canvasDimension);
        gameObjects = new ArrayList<>();

        this.collisionManager = new CollisionManager(tileMap, gameObjects);

        player = gameObjectFactory.createPlayer(canvasDimension, this, level.getStartPosition(), tileMap);
        this.addNewObject(player);

        level.getGameObjects().forEach(gameObject -> this.addNewObject(
                createGameObject(gameObject.getType(),
                new Vector2D(gameObject.getX(), gameObject.getY())))
        );

        audioManager.playBackgroundMusic("theme", true);
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
       sceneManager.openWinScene();
    }

    @Override
    public void update() {
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
        final Vector2D bgPosition = bg.getDrawingPosition();
        graphics.drawImage(bg.getDrawableImage(), bgPosition.getRoundedX(), bgPosition.getRoundedY(), canvasDimension.width(), canvasDimension.height(), null);

        for (final AbstractGameObject i:gameObjects) {
            if (!i.isDead()) {
                final Vector2D position = i.getDrawingPosition();
                final Vector2D spriteDimension = i.getSpriteDimension();

                graphics.drawImage(i.getDrawableImage(), position.getRoundedX(), position.getRoundedY(), spriteDimension.getRoundedX(), spriteDimension.getRoundedY(), null);
            }
        }

        final Vector2D tileMapPosition = tileMap.getDrawingPosition();
        graphics.drawImage(tileMap.getDrawableImage(), tileMapPosition.getRoundedX(), tileMapPosition.getRoundedY(), canvasDimension.width(), canvasDimension.height(), null);

        if(settingsProvider.getSettings().isShowFps()) {
            int currentFps = Time.getFps();

            if(currentFps < 30)
                graphics.setColor(Color.RED);
            else
                graphics.setColor(Color.GREEN);

            graphics.setFont(P_FONT);

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final String currentFpsString = String.valueOf(currentFps);
            final int fpsWidth = graphics.getFontMetrics().stringWidth(currentFpsString);
            graphics.drawString(currentFpsString, canvasDimension.width() - fpsWidth - FPS_OFFSET, graphics.getFontMetrics().getHeight());
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        return canvas;
    }

    @Override
    public void detach() {
        audioManager.stopMusic();
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(player != null) {
            player.keyPressed(event);
        }

        if (VK_ESCAPE == event.getKeyCode()) {
            isClosed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if(player != null) {
            player.keyReleased(event);
        }
    }
}
