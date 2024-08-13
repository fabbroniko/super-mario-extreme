package com.fabbroniko;

import com.fabbroniko.audio.AudioPlayer;
import com.fabbroniko.audio.AudioPlayerImpl;
import com.fabbroniko.audio.ResetClipLineListener;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.environment.SettingsProviderImpl;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.gameobjects.GameObjectFactoryImpl;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.GamePanel;
import com.fabbroniko.main.GameWindow;
import com.fabbroniko.resource.AudioLoader;
import com.fabbroniko.resource.CacheLessAudioLoader;
import com.fabbroniko.resource.CachedAudioLoader;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.SceneContextFactory;
import com.fabbroniko.scene.SceneContextFactoryImpl;
import com.fabbroniko.scene.factory.SceneFactory;
import com.fabbroniko.scene.factory.SceneFactoryImpl;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.ui.DrawableResourceFactoryImpl;
import com.fabbroniko.ui.OptionFactory;
import com.fabbroniko.ui.OptionFactoryImpl;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.background.BackgroundLoaderImpl;
import com.fabbroniko.ui.text.AwtTextFactory;
import com.fabbroniko.ui.text.FontProviderImpl;
import com.fabbroniko.ui.text.TextFactory;

import javax.sound.sampled.LineListener;
import java.awt.Toolkit;

public class Application {

    private static final int CANVAS_WIDTH = 1280;
    private static final int CANVAS_HEIGHT = 960;

    public static void main(final String[] args) {
        final ResourceManager resourceManager = new ResourceManager();
        final SettingsProvider settingsProvider = new SettingsProviderImpl(resourceManager);
        final SceneContextFactory sceneContextFactory = new SceneContextFactoryImpl(CANVAS_WIDTH, CANVAS_HEIGHT);
        final AudioPlayer audioPlayer = getAudioPlayer(resourceManager, settingsProvider);
        final TextFactory textFactory = new AwtTextFactory(new FontProviderImpl());
        final OptionFactory optionFactory = new OptionFactoryImpl(textFactory);
        final DrawableResourceFactory drawableResourceFactory = new DrawableResourceFactoryImpl();
        final BackgroundLoader backgroundLoader = new BackgroundLoaderImpl(resourceManager, drawableResourceFactory);
        final GameObjectFactory gameObjectFactory = new GameObjectFactoryImpl(audioPlayer, resourceManager, settingsProvider);
        final SceneFactory sceneFactory = new SceneFactoryImpl(sceneContextFactory, settingsProvider, audioPlayer, resourceManager, textFactory, optionFactory, backgroundLoader, gameObjectFactory);
        final Dimension2D canvasDimension = new Dimension2D(CANVAS_WIDTH, CANVAS_HEIGHT);
        final java.awt.Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension2D windowDimension = new Dimension2D((int) screenDimensions.getWidth(), (int) screenDimensions.getHeight());;
        final GamePanel gamePanel = new GamePanel(canvasDimension, windowDimension);
        new GameWindow(gamePanel);
        final GameManager gameManager = new GameManager(audioPlayer, gamePanel, resourceManager, settingsProvider, sceneFactory);

        gameManager.start();
    }

    private static AudioPlayer getAudioPlayer(final ResourceManager resourceManager, final SettingsProvider settingsProvider) {
        final LineListener lineListener = new ResetClipLineListener();
        final AudioLoader diskAudioLoader = new CacheLessAudioLoader(resourceManager, lineListener);
        final AudioLoader cachedAudioLoader = new CachedAudioLoader(diskAudioLoader);
        return new AudioPlayerImpl(settingsProvider, cachedAudioLoader);
    }

}
