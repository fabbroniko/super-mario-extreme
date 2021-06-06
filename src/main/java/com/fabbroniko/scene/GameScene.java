package com.fabbroniko.scene;

import com.fabbroniko.environment.*;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.Block;
import com.fabbroniko.gameobjects.Castle;
import com.fabbroniko.gameobjects.Enemy;
import com.fabbroniko.gameobjects.FallingBlock;
import com.fabbroniko.gameobjects.InvisibleBlock;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.Time;
import com.fabbroniko.resource.domain.Level;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * First Level.
 * @author com.fabbroniko
 */
public final class GameScene extends AbstractScene implements KeyListener {

    private static final int FPS_OFFSET = 15;

    private final Level level;
    private Background bg;
    private TileMap tileMap;
    private List<AbstractGameObject> gameObjects;
    private CollisionManager collisionManager;

    public GameScene(final GameManager gameManager, final Level level) {
        super(gameManager);

        this.level = level;
    }

    @Override
    public void init() {
        bg = new Background(gameManager.getResourceManager(), "game");
        tileMap = new TileMap(gameManager.getResourceManager(), level.getMap());
        gameObjects = new ArrayList<>();

        this.collisionManager = new CollisionManager(tileMap, gameObjects);

        final Player player = (Player) this.addNewObject(Player.class, level.getStartPosition().clone());

        gameManager.addKeyListener(player);
        gameManager.addKeyListener(this);

        level.getGameObjects().forEach(gameObject -> this.addNewObject(
                getClassFromName(gameObject.getType()),
                new Position(gameObject.getX(), gameObject.getY()))
        );

        audioManager.playBackgroundMusic("theme", true);
    }

    private Class<? extends AbstractGameObject> getClassFromName(final String name) {
        switch (name) {
            case "castle":
                return Castle.class;
            case "invisible-block":
                return InvisibleBlock.class;
            case "falling-platform":
                return FallingBlock.class;
            case "ghost-enemy":
                return Enemy.class;
            case "breakable-block":
                return Block.class;
            default:
                throw new IllegalArgumentException("The specified game object with name " + name + " doesn't exist.");
        }
    }

    public final void checkForCollisions(final AbstractGameObject obj, final Position offsetPosition) {
        this.collisionManager.checkForCollisions(obj, offsetPosition);
    }

    private AbstractGameObject addNewObject(final Class<? extends AbstractGameObject> objectClass, final Position position) {
        try {
            final AbstractGameObject newGameObject = objectClass.getConstructor(TileMap.class, GameScene.class, Position.class).newInstance(tileMap, this, position);
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
    public void detachScene() {
        super.detachScene();

        gameManager.removeKeyListener(this);
    }

    @Override
    public void update() {
        super.update();

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
    public void draw(final Graphics2D g, final Dimension gDimension) {
        bg.draw(g, gDimension);

        for (final AbstractGameObject i:gameObjects) {
            if (!i.isDead()) {
                i.draw(g, gDimension);
            }
        }

        tileMap.draw(g, gDimension);

        if(gameManager.getSettings().isShowFps()) {
            int currentFps = Time.getFps();

            if(currentFps < 30)
                g.setColor(Color.RED);
            else
                g.setColor(Color.GREEN);

            g.setFont(P_FONT);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final String currentFpsString = String.valueOf(currentFps);
            final int fpsWidth = g.getFontMetrics().stringWidth(currentFpsString);
            g.drawString(currentFpsString, gDimension.getWidth() - fpsWidth - FPS_OFFSET, g.getFontMetrics().getHeight());
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
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
