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
import com.fabbroniko.gameobjects.Block;
import com.fabbroniko.gameobjects.Castle;
import com.fabbroniko.gameobjects.Enemy;
import com.fabbroniko.gameobjects.FallingBlock;
import com.fabbroniko.gameobjects.InvisibleBlock;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.Time;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * First Level.
 * @author com.fabbroniko
 */
public final class GameScene extends AbstractScene implements KeyListener, Scene {

    private static final int FPS_OFFSET = 15;

    private final Level level;
    private Background bg;
    private TileMap tileMap;
    private List<AbstractGameObject> gameObjects;
    private CollisionManager collisionManager;

    private final SceneContextFactory sceneContextFactory;
    private final GameManager gameManager;
    private final AudioManager audioManager;
    private final ResourceManager resourceManager;

    private BufferedImage canvas;
    private Graphics2D graphics;
    private Dimension2D canvasDimension;

    public GameScene(final SceneContextFactory sceneContextFactory,
                     final GameManager gameManager,
                     final AudioManager audioManager,
                     final ResourceManager resourceManager,
                     final Level level) {

        this.sceneContextFactory = sceneContextFactory;
        this.gameManager = gameManager;
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
        this.level = level;
    }

    @Override
    public void init() {
        bg = new Background(resourceManager, "game");
        tileMap = new TileMap(resourceManager, level.getMap(), gameManager.getCanvasSize());
        gameObjects = new ArrayList<>();

        this.collisionManager = new CollisionManager(tileMap, gameObjects);

        final Player player = (Player) this.addNewObject(Player.class, level.getStartPosition().clone());

        gameManager.addKeyListener(player);
        gameManager.addKeyListener(this);

        level.getGameObjects().forEach(gameObject -> this.addNewObject(
                getClassFromName(gameObject.getType()),
                new Vector2D(gameObject.getX(), gameObject.getY()))
        );

        audioManager.playBackgroundMusic("theme", true);

        final SceneContext sceneContext = sceneContextFactory.create();
        this.canvas = sceneContext.getSceneCanvas();
        this.graphics = (Graphics2D) canvas.getGraphics();
        this.canvasDimension = sceneContext.getCanvasDimension();
    }

    private Class<? extends AbstractGameObject> getClassFromName(final String name) {
        return switch (name) {
            case "castle" -> Castle.class;
            case "invisible-block" -> InvisibleBlock.class;
            case "falling-platform" -> FallingBlock.class;
            case "ghost-enemy" -> Enemy.class;
            case "breakable-block" -> Block.class;
            default ->
                    throw new IllegalArgumentException("The specified game object with name " + name + " doesn't exist.");
        };
    }

    public final void checkForCollisions(final AbstractGameObject obj, final Vector2D offsetPosition) {
        this.collisionManager.checkForCollisions(obj, offsetPosition);
    }

    private AbstractGameObject addNewObject(final Class<? extends AbstractGameObject> objectClass, final Vector2D position) {
        try {
            final AbstractGameObject newGameObject = objectClass.getConstructor(
                    TileMap.class,
                    GameScene.class,
                    ResourceManager.class,
                    AudioManager.class,
                    Vector2D.class)
                    .newInstance(tileMap, this, resourceManager, audioManager, position);
            gameObjects.add(newGameObject);
            return newGameObject;
        } catch (final Exception e) {
            throw new com.fabbroniko.error.InstantiationException(objectClass, e);
        }
    }

    public void levelFinished() {
        gameManager.openScene(WinScene.class);
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
        graphics.drawImage(bg.getDrawableImage(), bgPosition.getRoundedX(), bgPosition.getRoundedY(), canvasDimension.getWidth(), canvasDimension.getHeight(), null);

        for (final AbstractGameObject i:gameObjects) {
            if (!i.isDead()) {
                final Vector2D position = i.getDrawingPosition();
                final Vector2D spriteDimension = i.getSpriteDimension();

                graphics.drawImage(i.getDrawableImage(), position.getRoundedX(), position.getRoundedY(), spriteDimension.getRoundedX(), spriteDimension.getRoundedY(), null);
            }
        }

        final Vector2D tileMapPosition = tileMap.getDrawingPosition();
        graphics.drawImage(tileMap.getDrawableImage(), tileMapPosition.getRoundedX(), tileMapPosition.getRoundedY(), canvasDimension.getWidth(), canvasDimension.getHeight(), null);

        if(gameManager.getSettings().isShowFps()) {
            int currentFps = Time.getFps();

            if(currentFps < 30)
                graphics.setColor(Color.RED);
            else
                graphics.setColor(Color.GREEN);

            graphics.setFont(P_FONT);

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final String currentFpsString = String.valueOf(currentFps);
            final int fpsWidth = graphics.getFontMetrics().stringWidth(currentFpsString);
            graphics.drawString(currentFpsString, canvasDimension.getWidth() - fpsWidth - FPS_OFFSET, graphics.getFontMetrics().getHeight());
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        return canvas;
    }

    @Override
    public void detach() {
        audioManager.stopMusic();
        gameManager.removeKeyListener(this);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameManager.getInstance().openScene(MainMenuScene.class);
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {}

    @Override
    public void keyTyped(final KeyEvent e) {}
}
