package com.fabbroniko.scene.mainmenu;

import com.fabbroniko.scene.SceneManager;

import java.util.List;

public class QuitSelectedState implements MainMenuState {

    private final SceneManager sceneManager;

    public QuitSelectedState(final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onConfirm() {
        sceneManager.quit();
    }

    @Override
    public List<MenuOption> getOptions() {
        return List.of(new MenuOptionImpl(false), new MenuOptionImpl(false), new MenuOptionImpl(true));
    }
}
