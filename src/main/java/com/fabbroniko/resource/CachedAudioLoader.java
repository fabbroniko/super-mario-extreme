package com.fabbroniko.resource;

import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class CachedAudioLoader implements AudioLoader {

    private final Map<String, Clip> cache;
    private final AudioLoader alternateSource;

    public CachedAudioLoader(final AudioLoader alternateSource) {
        this.alternateSource = alternateSource;
        this.cache = new HashMap<>();
    }

    @Override
    public Optional<Clip> findClipByName(final String name) {
        if(cache.containsKey(name)) {
            final Clip clip = cache.get(name);

            clip.stop();
            clip.setFramePosition(0);

            log.info("Fetched clip named {} from cache", name);
            return Optional.of(clip);
        }

        Optional<Clip> alternate = alternateSource.findClipByName(name);
        alternate.ifPresent(clip -> cache.put(name, clip));
        return alternate;
    }
}
