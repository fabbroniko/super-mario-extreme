package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Position;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.context.ApplicationContext;

@Component
public class GameObjectFactoryImpl implements GameObjectFactory {

    private final ApplicationContext applicationContext;

    public GameObjectFactoryImpl(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Player createPlayer(final Position initialPosition) {
        final Player player = applicationContext.getInstance(Player.class);
        player.setInitialPosition(initialPosition);
        return player;
    }

    @Override
    public GameObject createCastle(final Position initialPosition) {
        final GameObject gameObject = applicationContext.getInstance(Castle.class);
        gameObject.setInitialPosition(initialPosition);
        return gameObject;
    }

    @Override
    public GameObject createEnemy(final Position initialPosition) {
        final GameObject gameObject = applicationContext.getInstance(Enemy.class);
        gameObject.setInitialPosition(initialPosition);
        return gameObject;
    }

    @Override
    public GameObject createBlock(final Position initialPosition) {
        final GameObject gameObject = applicationContext.getInstance(Block.class);
        gameObject.setInitialPosition(initialPosition);
        return gameObject;
    }
}
