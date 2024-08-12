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
    private final TextFactory textFactory;
    private final OptionFactory optionFactory;

    public SceneFactoryImpl(final SceneContextFactory sceneContextFactory,
                            final SettingsProvider settingsProvider,
                            final AudioManager audioManager,
                            final ResourceManager resourceManager,
                            final TextFactory textFactory,
                            final OptionFactory optionFactory) {

        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
        this.textFactory = textFactory;
        this.optionFactory = optionFactory;
    }

    @Override
    public Scene createMainMenuScene(final SceneManager sceneManager) {
        return new MainMenuScene(sceneContextFactory, resourceManager, sceneManager, textFactory, optionFactory);
    }

    @Override
    public Scene createSettingsScene(final SceneManager sceneManager) {
        return new SettingsMenuScene(sceneContextFactory, settingsProvider, sceneManager, resourceManager, textFactory);
    }

    @SneakyThrows
    @Override
    public Scene createGameScene(final SceneManager sceneManager) {
        final Level level = new XmlMapper().readValue(getClass().getResource("/levels/testing.xml"), Level.class);
        return new GameScene(sceneContextFactory, settingsProvider, audioManager, resourceManager, sceneManager, textFactory, level);
    }

    @Override
    public Scene createLostScene(final SceneManager sceneManager, final int deathCount) {
        return new LostScene(sceneContextFactory, audioManager, sceneManager, textFactory, deathCount);
    }

    @Override
    public Scene createWinScene(final SceneManager sceneManager) {
        return new WinScene(sceneContextFactory, audioManager, sceneManager, textFactory);
    }
}
