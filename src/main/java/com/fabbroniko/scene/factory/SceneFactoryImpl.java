package com.fabbroniko.scene.factory;

import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.resource.dto.LevelDto;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.SettingsMenuScene;
import com.fabbroniko.scene.WinScene;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

@Component
public class SceneFactoryImpl implements SceneFactory {

    private final SceneContextFactory sceneContextFactory;
    private final SettingsProvider settingsProvider;
    private final MusicPlayer musicPlayer;
    private final ImageLoader imageLoader;
    private final TextFactory textFactory;
    private final BackgroundLoader backgroundLoader;
    private final GameObjectFactory gameObjectFactory;
    private final ApplicationContext applicationContext;

    public SceneFactoryImpl(final SceneContextFactory sceneContextFactory,
                            final SettingsProvider settingsProvider,
                            final MusicPlayer musicPlayer,
                            @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
                            final TextFactory textFactory,
                            final BackgroundLoader backgroundLoader, GameObjectFactory gameObjectFactory,
                            final ApplicationContext applicationContext) {

        this.sceneContextFactory = sceneContextFactory;
        this.settingsProvider = settingsProvider;
        this.musicPlayer = musicPlayer;
        this.imageLoader = imageLoader;
        this.textFactory = textFactory;
        this.backgroundLoader = backgroundLoader;
        this.gameObjectFactory = gameObjectFactory;
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
        final LevelDto level = new XmlMapper().readValue(getClass().getResource("/levels/testing.xml"), LevelDto.class);
        return new GameScene(sceneContextFactory, settingsProvider, musicPlayer, imageLoader, sceneManager, textFactory, backgroundLoader, gameObjectFactory, level);
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
