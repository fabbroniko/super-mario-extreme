package com.fabbroniko.scene;

import com.fabbroniko.environment.*;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.gameobjects.Block;
import com.fabbroniko.gameobjects.Castle;
import com.fabbroniko.gameobjects.Enemy;
import com.fabbroniko.gameobjects.FallingBlock;
import com.fabbroniko.gameobjects.GameObjectBuilder;
import com.fabbroniko.gameobjects.InvisibleBlock;
import com.fabbroniko.gameobjects.Player;
import com.fabbroniko.GameManager;
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

    private final Level level;
    private GameObjectBuilder gameObjectBuilder;
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

        gameObjectBuilder = new GameObjectBuilder(tileMap, this);

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
        final AbstractGameObject newGameObject = gameObjectBuilder.newInstance(objectClass).setPosition(position).getInstance();
        gameObjects.add(newGameObject);

        return newGameObject;
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

        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update();
            if (gameObjects.get(i).isDead()) {
                gameObjects.remove(i); // TODO refactor
            }
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

        drawFps(g, gDimension);
    }

    private void drawFps(final Graphics2D g, final Dimension dimension) {
        if(gameManager.getSettings().isShowFps()) {
            int currentFps = Time.getFps();

            if(currentFps < 30)
                g.setColor(Color.RED);
            else
                g.setColor(Color.GREEN);

            g.setFont(g.getFont().deriveFont(14.0f));

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawString(String.valueOf(Time.getFps()), 292, 15);
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
