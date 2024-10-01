package com.fabbroniko.scene.factory;

import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.SettingsMenuScene;
import com.fabbroniko.scene.WinScene;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.context.ApplicationContext;
import lombok.SneakyThrows;

@Component
public class SceneFactoryImpl implements SceneFactory {

    private final ApplicationContext applicationContext;

    public SceneFactoryImpl(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Scene createMainMenuScene(final SceneManager sceneManager) {
        return applicationContext.getInstance(MainMenuScene.class);
    }

    @Override
    public Scene createSettingsScene(final SceneManager sceneManager) {
        return applicationContext.getInstance(SettingsMenuScene.class);
    }

    @SneakyThrows
    @Override
    public Scene createGameScene(final SceneManager sceneManager) {
        return applicationContext.getInstance(GameScene.class);
    }

    @Override
    public Scene createLostScene(final SceneManager sceneManager) {
        return applicationContext.getInstance(LostScene.class);
    }

    @Override
    public Scene createWinScene(final SceneManager sceneManager) {
        return applicationContext.getInstance(WinScene.class);
    }
}
