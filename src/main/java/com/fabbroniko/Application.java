package com.fabbroniko;

import com.fabbroniko.audio.AudioFactoryImpl;
import com.fabbroniko.audio.AudioLoader;
import com.fabbroniko.audio.CachedAudioLoader;
import com.fabbroniko.audio.DiskAudioLoader;
import com.fabbroniko.audio.EffectPlayer;
import com.fabbroniko.audio.EffectPlayerImpl;
import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.audio.MusicPlayerImpl;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.main.BridgedKeyListener;
import com.fabbroniko.main.ScreenSizeResolver;
import com.fabbroniko.main.FPSGameCycle;
import com.fabbroniko.main.CustomKeyListener;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.settings.SettingsProviderImpl;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.gameobjects.GameObjectFactoryImpl;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.GamePanel;
import com.fabbroniko.resource.AudioResourceLocator;
import com.fabbroniko.resource.CachedImageLoader;
import com.fabbroniko.resource.DefaultPathToUrlConverter;
import com.fabbroniko.resource.DefaultResourceLoader;
import com.fabbroniko.resource.DiskImageLoader;
import com.fabbroniko.resource.DiskResourceIndexLoader;
import com.fabbroniko.settings.DiskUserSettingsLoader;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.resource.PathToUrlConverter;
import com.fabbroniko.resource.ResourceIndexLoader;
import com.fabbroniko.settings.UserSettingsLoader;
import com.fabbroniko.resource.dto.ResourceDto;
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

import java.util.HashMap;

public class Application {

    private static final int CANVAS_WIDTH = 1280;
    private static final int CANVAS_HEIGHT = 960;
    private static final String RESOURCE_INDEX_LOCATION = "/resources.index";

    public static void main(final String[] args) {
        final ResourceDto resourceDto = getResource();
        final ImageLoader imageLoader = imageLoader(resourceDto);
        final SettingsProvider settingsProvider = settingsProvider();
        final Dimension2D canvasSize = new ImmutableDimension2D(CANVAS_WIDTH, CANVAS_HEIGHT);
        final SceneContextFactory sceneContextFactory = new SceneContextFactoryImpl(canvasSize);
        final AudioLoader diskAudioLoader = new DiskAudioLoader(new DefaultResourceLoader(), new AudioResourceLocator(resourceDto), new AudioFactoryImpl());
        final AudioLoader cachedAudioLoader = new CachedAudioLoader(diskAudioLoader, new HashMap<>());
        final EffectPlayer effectPlayer = new EffectPlayerImpl(settingsProvider, cachedAudioLoader);
        final MusicPlayer musicPlayer = new MusicPlayerImpl(settingsProvider, cachedAudioLoader);
        final TextFactory textFactory = new AwtTextFactory(new FontProviderImpl());
        final OptionFactory optionFactory = new OptionFactoryImpl(textFactory);
        final DrawableResourceFactory drawableResourceFactory = new DrawableResourceFactoryImpl();
        final BackgroundLoader backgroundLoader = new BackgroundLoaderImpl(imageLoader, drawableResourceFactory);
        final GameObjectFactory gameObjectFactory = new GameObjectFactoryImpl(effectPlayer, imageLoader, settingsProvider);
        final SceneFactory sceneFactory = new SceneFactoryImpl(sceneContextFactory, settingsProvider, musicPlayer, imageLoader, textFactory, optionFactory, backgroundLoader, gameObjectFactory);

        final CustomKeyListener customKeyListener = new BridgedKeyListener();
        final GamePanel gamePanel = getGamePanel(customKeyListener);
        final GameManager gameManager = new GameManager(gamePanel, sceneFactory, new FPSGameCycle(settingsProvider), customKeyListener);
        gameManager.start();
    }

    private static SettingsProvider settingsProvider() {
        final ObjectMapper jsonMapper = new ObjectMapper();
        final UserSettingsLoader userSettingsLoader = new DiskUserSettingsLoader(jsonMapper);
        return new SettingsProviderImpl(userSettingsLoader);
    }

    private static ResourceDto getResource() {
        final PathToUrlConverter pathToUrlConverter = new DefaultPathToUrlConverter();
        final ObjectMapper xmlMapper = new XmlMapper();
        final ResourceIndexLoader diskResourceIndexLoader = new DiskResourceIndexLoader(xmlMapper, pathToUrlConverter);
        return diskResourceIndexLoader.load(RESOURCE_INDEX_LOCATION);
    }

    private static ImageLoader imageLoader(final ResourceDto resourceDto) {
        final ImageLoader diskImageLoader = new DiskImageLoader(resourceDto);
        return new CachedImageLoader(diskImageLoader);
    }

    private static GamePanel getGamePanel(final CustomKeyListener customKeyListener) {
        return new GamePanel(new ScreenSizeResolver(), customKeyListener);
    }
}
