package com.fabbroniko;

import com.fabbroniko.audio.AudioFactoryImpl;
import com.fabbroniko.audio.AudioLoader;
import com.fabbroniko.audio.CachedAudioLoader;
import com.fabbroniko.audio.DiskAudioLoader;
import com.fabbroniko.audio.EffectPlayer;
import com.fabbroniko.audio.EffectPlayerImpl;
import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.audio.MusicPlayerImpl;
import com.fabbroniko.environment.CanvasSizeDimension;
import com.fabbroniko.gameobjects.GameObjectFactory;
import com.fabbroniko.gameobjects.GameObjectFactoryImpl;
import com.fabbroniko.main.BridgedKeyListener;
import com.fabbroniko.main.CustomKeyListener;
import com.fabbroniko.main.FPSGameCycle;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.ScreenSizeResolver;
import com.fabbroniko.main.SwingGameRenderer;
import com.fabbroniko.mapper.JsonMapperSupplier;
import com.fabbroniko.mapper.XmlMapperSupplier;
import com.fabbroniko.resource.AudioResourceLocator;
import com.fabbroniko.resource.CachedImageLoader;
import com.fabbroniko.resource.DefaultPathToUrlConverter;
import com.fabbroniko.resource.DefaultResourceLoader;
import com.fabbroniko.resource.DiskImageLoader;
import com.fabbroniko.resource.DiskResourceIndexLoader;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.resource.PathToUrlConverter;
import com.fabbroniko.resource.ResourceIndexLoader;
import com.fabbroniko.resource.dto.PreLoadedResource;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.scene.factory.SceneContextFactoryImpl;
import com.fabbroniko.scene.factory.SceneFactory;
import com.fabbroniko.scene.factory.SceneFactoryImpl;
import com.fabbroniko.settings.DiskUserSettingsLoader;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.settings.SettingsProviderImpl;
import com.fabbroniko.settings.UserSettingsLoader;
import com.fabbroniko.ui.DrawableResourceFactory;
import com.fabbroniko.ui.DrawableResourceFactoryImpl;
import com.fabbroniko.ui.OptionFactory;
import com.fabbroniko.ui.OptionFactoryImpl;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.background.BackgroundLoaderImpl;
import com.fabbroniko.ui.text.AwtTextFactory;
import com.fabbroniko.ui.text.FontProviderImpl;
import com.fabbroniko.ui.text.TextFactory;

import java.util.HashMap;

public class Application {

    public static void main(final String[] args) {
        final PathToUrlConverter pathToUrlConverter = new DefaultPathToUrlConverter();
        final ResourceIndexLoader resourceIndexLoader = new DiskResourceIndexLoader(new XmlMapperSupplier(), pathToUrlConverter);
        final PreLoadedResource preLoadedResource = new PreLoadedResource(resourceIndexLoader);
        final ImageLoader diskImageLoader = new DiskImageLoader(preLoadedResource);
        final ImageLoader imageLoader = new CachedImageLoader(diskImageLoader);
        final UserSettingsLoader userSettingsLoader = new DiskUserSettingsLoader(new JsonMapperSupplier());
        final SettingsProvider settingsProvider = new SettingsProviderImpl(userSettingsLoader);
        final SceneContextFactory sceneContextFactory = new SceneContextFactoryImpl(new CanvasSizeDimension());
        final AudioLoader diskAudioLoader = new DiskAudioLoader(new DefaultResourceLoader(), new AudioResourceLocator(preLoadedResource), new AudioFactoryImpl());
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
        final SwingGameRenderer gameRenderer = new SwingGameRenderer(new ScreenSizeResolver(), customKeyListener);
        final GameManager gameManager = new GameManager(gameRenderer, sceneFactory, new FPSGameCycle(settingsProvider), customKeyListener);
        gameManager.start();
    }
}
