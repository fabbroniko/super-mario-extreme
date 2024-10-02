package com.fabbroniko.audio;

import com.fabbroniko.error.ResourceLoadingException;
import com.fabbroniko.resource.ResourceLoader;
import com.fabbroniko.resource.ResourceLocator;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.ul.Logger;

import javax.sound.sampled.Clip;
import java.io.InputStream;

@Component
public class DiskAudioLoader implements AudioLoader {

    private final ResourceLoader resourceLoader;
    private final ResourceLocator resourceLocator;
    private final AudioFactory audioFactory;
    private final Logger logger;

    public DiskAudioLoader(final ResourceLoader resourceLoader,
                           final ResourceLocator resourceLocator,
                           final AudioFactory audioFactory,
                           final Logger logger) {

        this.resourceLoader = resourceLoader;
        this.resourceLocator = resourceLocator;
        this.audioFactory = audioFactory;
        this.logger = logger;
    }

    @Override
    public Clip loadClipByName(final String name) {
        final String path = resourceLocator.findByName(name);
        logger.trace("loading_clip", name, path);

        try (final InputStream inputStream = resourceLoader.load(path)) {
            final Clip clip = audioFactory.createClip();
            clip.open(audioFactory.createAudioInputStream(inputStream));

            logger.info("loaded_clip", name);
           return clip;
        } catch (final Exception exception) {
            logger.fatal("clip_loading_exception", exception, name);
            throw new ResourceLoadingException(exception);
        }
    }
}
