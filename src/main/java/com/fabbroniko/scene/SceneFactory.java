package com.fabbroniko.scene;

import com.fabbroniko.main.SceneManager;

public interface SceneFactory {

    Scene createMainMenuScene(final SceneManager sceneManager);

    Scene createSettingsScene();

    Scene createGameScene(final SceneManager sceneManager);

    Scene createLostScene();

    Scene createWinScene();
}
