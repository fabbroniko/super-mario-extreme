package com.fabbroniko.audio;

import com.fabbroniko.error.ResourceLoadingException;
import com.fabbroniko.resource.ResourceLoader;
import com.fabbroniko.resource.ResourceLocator;

import javax.sound.sampled.Clip;
import java.io.InputStream;

public class DiskAudioLoader implements AudioLoader {

    private final ResourceLoader resourceLoader;
    private final ResourceLocator resourceLocator;
    private final AudioFactory audioFactory;
    private final ClipConfigurator clipConfigurator;

    public DiskAudioLoader(final ResourceLoader resourceLoader,
                           final ResourceLocator resourceLocator,
                           final AudioFactory audioFactory,
                           final ClipConfigurator clipConfigurator) {

        this.resourceLoader = resourceLoader;
        this.resourceLocator = resourceLocator;
        this.audioFactory = audioFactory;
        this.clipConfigurator = clipConfigurator;
    }

    @Override
    public Clip loadClipByName(final String name) {
        final String path = resourceLocator.findByName(name);

        try (final InputStream inputStream = resourceLoader.load(path)) {
            final Clip clip = audioFactory.createClip();
            clip.open(audioFactory.createAudioInputStream(inputStream));

           return clipConfigurator.configureLineListener(clip);
        } catch (final Exception exception) {
            throw new ResourceLoadingException(exception);
        }
    }
}
