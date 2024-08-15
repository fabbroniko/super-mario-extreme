package com.fabbroniko.scene.factory;

import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.resource.dto.Level;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.SettingsMenuScene;
import com.fabbroniko.scene.WinScene;
import com.fabbroniko.ui.OptionFactory;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public class SceneFactoryImpl implements SceneFactory {

    private final SceneContextFactory sceneContextFactory;
    private final SettingsProvider settingsProvider;
    private final MusicPlayer musicPlayer;
    private final ImageLoader imageLoader;
    private final TextFactory textFactory;
    private final OptionFactory optionFactory;
    private final BackgroundLoader backgroundLoader;
    private final GameObjectFactory gameObjectFactory;

    public SceneFactoryImpl(final SceneContextFactory sceneContextFactory,
                            final SettingsProvider settingsProvider,
                            final MusicPlayer musicPlayer,
                            final ImageLoader imageLoader,
                            final TextFactory textFactory,
                            final OptionFactory optionFactory,
                            final BackgroundLoader backgroundLoader, GameObjectFactory gameObjectFactory) {

        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.musicPlayer = musicPlayer;
        this.imageLoader = imageLoader;
        this.textFactory = textFactory;
        this.optionFactory = optionFactory;
        this.backgroundLoader = backgroundLoader;
        this.gameObjectFactory = gameObjectFactory;
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
        return new GameScene(sceneContextFactory, settingsProvider, musicPlayer, imageLoader, sceneManager, textFactory, backgroundLoader, gameObjectFactory, level);
    }

    @Override
    public Scene createLostScene(final SceneManager sceneManager, final int deathCount) {
        return new LostScene(sceneContextFactory, musicPlayer, sceneManager, textFactory, deathCount, backgroundLoader);
    }

    @Override
    public Scene createWinScene(final SceneManager sceneManager) {
        return new WinScene(sceneContextFactory, musicPlayer, sceneManager, textFactory, backgroundLoader);
    }
}
