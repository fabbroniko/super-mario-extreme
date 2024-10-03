package com.fabbroniko.audio;

import com.fabbroniko.resource.ResourceCache;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.ul.Logger;
import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class AudioClipCache implements ResourceCache<Clip> {

    private final Map<String, Clip> cache;
    private final Logger logger;

    public AudioClipCache(final Logger logger) {
        this(new HashMap<>(), logger);
    }

    public AudioClipCache(final Map<String, Clip> cache, final Logger logger) {
        this.cache = cache;
        this.logger = logger;
    }

    @Override
    public Optional<Clip> findByName(final String name) {
        return Optional.ofNullable(cache.get(name));
    }

    @Override
    public synchronized void insert(final String name, final Clip resource) {
        if (cache.containsKey(name)) {
            logger.error("insert_audio_clip_in_cache_already_registered", name);
            return;
        }

        cache.put(name, resource);
    }
}
