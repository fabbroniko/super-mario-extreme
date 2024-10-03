package com.fabbroniko.audio;

import com.fabbroniko.resource.ResourceCache;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.ul.Logger;

import javax.sound.sampled.Clip;
import java.util.Optional;

@Component
public class CachedAudioLoader implements AudioLoader {

    private final ResourceCache<Clip> cache;
    private final AudioLoader alternativeAudioLoader;
    private final Logger logger;

    public CachedAudioLoader(@Qualifier("diskAudioLoader") final AudioLoader alternativeAudioLoader,
                             final ResourceCache<Clip> cache,
                             final Logger logger) {
        this.alternativeAudioLoader = alternativeAudioLoader;
        this.cache = cache;
        this.logger = logger;
    }

    @Override
    public Clip loadClipByName(final String name) {
        final Optional<Clip> cachedClip = cache.findByName(name);
        if (cachedClip.isPresent()) {
            logger.trace("audio_clip_cache_hit", name);
            return cachedClip.get();
        }

        logger.info("audio_clip_cache_miss", name);
        final Clip loadedClip = alternativeAudioLoader.loadClipByName(name);
        cache.insert(name, loadedClip);
        return loadedClip;
    }
}
