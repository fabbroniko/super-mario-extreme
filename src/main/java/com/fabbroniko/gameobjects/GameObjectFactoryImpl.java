package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Position;
import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.ResourceType;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameObjectFactoryImpl implements GameObjectFactory {

    private final Map<String, Class<? extends GameObject>> registry;
    private final ApplicationContext applicationContext;

    public GameObjectFactoryImpl(final ApplicationContext applicationContext) {
        this.registry = new HashMap<>();
        this.registry.put("castle", Castle.class);
        this.registry.put("ghost-enemy", Enemy.class);
        this.registry.put("breakable-block", Block.class);
        this.registry.put("player", Player.class);
        this.applicationContext = applicationContext;
    }

    @Override
    public GameObject create(final String name, final Position initialPosition) {
        if (!registry.containsKey(name)) {
            throw new UndefinedResourceException(name, ResourceType.GAME_OBJECT);
        }

        final GameObject gameObject = applicationContext.getInstance(registry.get(name));
        gameObject.setInitialPosition(initialPosition);
        return gameObject;
    }
}
