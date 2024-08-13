package com.fabbroniko.scene.factory;

import com.fabbroniko.audio.AudioManager;
import com.fabbroniko.scene.SceneContextFactory;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SettingsMenuScene;
import com.fabbroniko.scene.WinScene;
import com.fabbroniko.ui.OptionFactory;
import com.fabbroniko.ui.text.TextFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public class SceneFactoryImpl implements SceneFactory {

    private final SceneContextFactory sceneContextFactory;
    private final SettingsProvider settingsProvider;
    private final AudioManager audioManager;
    private final ResourceManager resourceManager;
    private final TextFactory textFactory;
    private final OptionFactory optionFactory;
    private final BackgroundLoader backgroundLoader;

    public SceneFactoryImpl(final SceneContextFactory sceneContextFactory,
                            final SettingsProvider settingsProvider,
                            final AudioManager audioManager,
                            final ResourceManager resourceManager,
                            final TextFactory textFactory,
                            final OptionFactory optionFactory,
                            final BackgroundLoader backgroundLoader) {

        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
        this.textFactory = textFactory;
        this.optionFactory = optionFactory;
        this.backgroundLoader = backgroundLoader;
    }

    @Override
    public Scene createMainMenuScene(final SceneManager sceneManager) {
        return new MainMenuScene(sceneContextFactory, sceneManager, textFactory, optionFactory, backgroundLoader);
    }

    @Override
    public Scene createSettingsScene(final SceneManager sceneManager) {
        return new SettingsMenuScene(sceneContextFactory, settingsProvider, sceneManager, textFactory, backgroundLoader);
    }

    @SneakyThrows
    @Override
    public Scene createGameScene(final SceneManager sceneManager) {
        final Level level = new XmlMapper().readValue(getClass().getResource("/levels/testing.xml"), Level.class);
        return new GameScene(sceneContextFactory, settingsProvider, audioManager, resourceManager, sceneManager, textFactory, backgroundLoader, level);
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
