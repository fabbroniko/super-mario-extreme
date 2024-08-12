package com.fabbroniko.scene;

import com.fabbroniko.main.SceneManager;

public interface SceneFactory {

    Scene createMainMenuScene(final SceneManager sceneManager);

    Scene createSettingsScene(final SceneManager sceneManager);

    Scene createGameScene(final SceneManager sceneManager);

    Scene createLostScene(final SceneManager sceneManager, final int deathCount);

    Scene createWinScene(final SceneManager sceneManager);
}
