package com.fabbroniko.scene.factory;

import com.fabbroniko.main.SceneManager;
import com.fabbroniko.scene.Scene;

public interface SceneFactory {

    Scene createMainMenuScene(final SceneManager sceneManager);

    Scene createSettingsScene(final SceneManager sceneManager);

    Scene createGameScene(final SceneManager sceneManager);

    Scene createLostScene(final SceneManager sceneManager, final int deathCount);

    Scene createWinScene(final SceneManager sceneManager);
}
