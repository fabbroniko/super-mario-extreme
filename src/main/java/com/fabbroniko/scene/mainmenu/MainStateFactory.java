package com.fabbroniko.scene.mainmenu;

import com.fabbroniko.environment.CircularState;
import com.fabbroniko.scene.SceneManager;

import java.util.ArrayDeque;
import java.util.List;

public class MainStateFactory implements StateFactory<MainMenuState> {

    private final SceneManager sceneManager;

    public MainStateFactory(final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public CircularState<MainMenuState> create() {
        final MainMenuState mainMenu = new StartGameSelectedState(sceneManager);
        final MainMenuState settings = new OpenSettingsSelectedState(sceneManager);
        final MainMenuState quit = new QuitSelectedState(sceneManager);
        return new CircularState<>(new ArrayDeque<>(List.of(mainMenu, settings, quit)));
    }
}
