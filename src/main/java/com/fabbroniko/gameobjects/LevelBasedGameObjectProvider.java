package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.LevelProvider;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.resource.dto.LevelDto;
import com.fabbroniko.sdi.annotation.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LevelBasedGameObjectProvider implements GameObjectProvider {

    private final LevelProvider levelProvider;
    private final GameObjectFactory gameObjectFactory;

    public LevelBasedGameObjectProvider(final LevelProvider levelProvider, final GameObjectFactory gameObjectFactory) {
        this.levelProvider = levelProvider;
        this.gameObjectFactory = gameObjectFactory;
    }

    @Override
    public List<GameObject> get() {
        final LevelDto level = levelProvider.getLevel();
        return level.getGameObjects()
            .stream()
            .map(gameObject -> gameObjectFactory.create(gameObject.getType(), new Vector2D(gameObject.getX(), gameObject.getY())))
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
