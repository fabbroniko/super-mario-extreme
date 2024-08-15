package com.fabbroniko;

import com.fabbroniko.audio.EffectPlayer;
import com.fabbroniko.audio.EffectPlayerImpl;
import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.audio.MusicPlayerImpl;
import com.fabbroniko.audio.line.ResetClipLineListener;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.environment.SettingsProviderImpl;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.gameobjects.GameObjectFactoryImpl;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.GamePanel;
import com.fabbroniko.main.GameWindow;
import com.fabbroniko.resource.AudioLoader;
import com.fabbroniko.resource.DiskAudioLoader;
import com.fabbroniko.resource.CachedAudioLoader;
import com.fabbroniko.resource.CachedImageLoader;
import com.fabbroniko.resource.CachedResourceIndexLoader;
import com.fabbroniko.resource.DefaultResourceLocator;
import com.fabbroniko.resource.DiskImageLoader;
import com.fabbroniko.resource.DiskResourceIndexLoader;
import com.fabbroniko.resource.DiskUserSettingsLoader;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.resource.ResourceIndexLoader;
import com.fabbroniko.resource.ResourceLocator;
import com.fabbroniko.resource.UserSettingsLoader;
import com.fabbroniko.resource.dto.Resource;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.scene.factory.SceneContextFactoryImpl;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.sound.sampled.LineListener;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Application {

    private static final int CANVAS_WIDTH = 1280;
    private static final int CANVAS_HEIGHT = 960;
    private static final String RESOURCE_INDEX_LOCATION = "/resources.index";

    public static void main(final String[] args) {
        final Resource resource = getResource();
        final ImageLoader imageLoader = imageLoader(resource);
        final SettingsProvider settingsProvider = settingsProvider();
        final SceneContextFactory sceneContextFactory = new SceneContextFactoryImpl(CANVAS_WIDTH, CANVAS_HEIGHT);
        final LineListener lineListener = new ResetClipLineListener();
        final AudioLoader diskAudioLoader = new DiskAudioLoader(resource, lineListener);
        final AudioLoader cachedAudioLoader = new CachedAudioLoader(diskAudioLoader);
        final EffectPlayer effectPlayer = new EffectPlayerImpl(settingsProvider, cachedAudioLoader);
        final MusicPlayer musicPlayer = new MusicPlayerImpl(settingsProvider, cachedAudioLoader);
        final TextFactory textFactory = new AwtTextFactory(new FontProviderImpl());
        final OptionFactory optionFactory = new OptionFactoryImpl(textFactory);
        final DrawableResourceFactory drawableResourceFactory = new DrawableResourceFactoryImpl();
        final BackgroundLoader backgroundLoader = new BackgroundLoaderImpl(imageLoader, drawableResourceFactory);
        final GameObjectFactory gameObjectFactory = new GameObjectFactoryImpl(effectPlayer, imageLoader, settingsProvider);
        final SceneFactory sceneFactory = new SceneFactoryImpl(sceneContextFactory, settingsProvider, musicPlayer, imageLoader, textFactory, optionFactory, backgroundLoader, gameObjectFactory);

        final GamePanel gamePanel = getGamePanel();
        final GameManager gameManager = new GameManager(gamePanel, settingsProvider, sceneFactory);
        gameManager.start();
    }

    private static SettingsProvider settingsProvider() {
        final ObjectMapper jsonMapper = new ObjectMapper();
        final UserSettingsLoader userSettingsLoader = new DiskUserSettingsLoader(jsonMapper);
        return new SettingsProviderImpl(userSettingsLoader);
    }

    private static Resource getResource() {
        final ResourceLocator resourceLocator = new DefaultResourceLocator();
        final ObjectMapper xmlMapper = new XmlMapper();
        final ResourceIndexLoader diskResourceIndexLoader = new DiskResourceIndexLoader(xmlMapper, resourceLocator);
        final ResourceIndexLoader cachedResourceIndexLoader = new CachedResourceIndexLoader(diskResourceIndexLoader);
        return cachedResourceIndexLoader.load(RESOURCE_INDEX_LOCATION);
    }

    private static ImageLoader imageLoader(final Resource resource) {
        final ImageLoader diskImageLoader = new DiskImageLoader(resource);
        return new CachedImageLoader(diskImageLoader);
    }

    private static GamePanel getGamePanel() {
        final Dimension2D canvasDimension = new Dimension2D(CANVAS_WIDTH, CANVAS_HEIGHT);
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension2D windowDimension = new Dimension2D((int) screenDimensions.getWidth(), (int) screenDimensions.getHeight());
        final GamePanel gamePanel = new GamePanel(canvasDimension, windowDimension);
        new GameWindow(gamePanel);
        return gamePanel;
    }
}
