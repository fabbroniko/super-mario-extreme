package com.fabbroniko.scene.mainmenu;

import com.fabbroniko.scene.SceneManager;

import java.util.List;

public class StartGameSelectedState implements MainMenuState {

    private final SceneManager sceneManager;

    public StartGameSelectedState(final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onConfirm() {
        sceneManager.openGameScene();
    }

    @Override
    public List<MenuOption> getOptions() {
        return List.of(new MenuOptionImpl(true), new MenuOptionImpl(false), new MenuOptionImpl(false));
    }
}
