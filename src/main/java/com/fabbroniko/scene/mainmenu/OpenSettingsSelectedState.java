package com.fabbroniko.scene.mainmenu;

import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.SettingsMenuScene;

import java.util.List;

public class OpenSettingsSelectedState implements MainMenuState {

    private final SceneManager sceneManager;

    public OpenSettingsSelectedState(final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onConfirm() {
        sceneManager.openScene(SettingsMenuScene.class);
    }

    @Override
    public List<MenuOption> getOptions() {
        return List.of(new MenuOptionImpl(false), new MenuOptionImpl(true), new MenuOptionImpl(false));
    }
}
