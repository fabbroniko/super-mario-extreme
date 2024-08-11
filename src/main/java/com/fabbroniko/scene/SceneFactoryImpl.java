package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.main.SceneManager;
import com.fabbroniko.main.SettingsProvider;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public class SceneFactoryImpl implements SceneFactory {

    private final SceneContextFactory sceneContextFactory;
    private final SettingsProvider settingsProvider;
    private final AudioManager audioManager;
    private final ResourceManager resourceManager;

    public SceneFactoryImpl(final SceneContextFactory sceneContextFactory,
                            final SettingsProvider settingsProvider,
                            final AudioManager audioManager,
                            final ResourceManager resourceManager) {

        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
    }

    @Override
    public Scene createMainMenuScene(final SceneManager sceneManager) {
        return new MainMenuScene(sceneContextFactory, audioManager, resourceManager, sceneManager);
    }

    @Override
    public Scene createSettingsScene() {
        return new SettingsMenuScene(sceneContextFactory, settingsProvider, audioManager, resourceManager);
    }

    @SneakyThrows
    @Override
    public Scene createGameScene(final SceneManager sceneManager) {
        final Level level = new XmlMapper().readValue(getClass().getResource("/levels/testing.xml"), Level.class);
        return new GameScene(sceneContextFactory, settingsProvider, audioManager, resourceManager, sceneManager, level);
    }

    @Override
    public Scene createLostScene() {
        return new LostScene(sceneContextFactory, audioManager);
    }

    @Override
    public Scene createWinScene() {
        return new WinScene(sceneContextFactory, audioManager);
    }
}
