package com.fabbroniko.audio;

import javax.sound.sampled.Clip;
import java.util.Map;

public class CachedAudioLoader implements AudioLoader {

    private final Map<String, Clip> cache;
    private final AudioLoader alternativeAudioLoader;

    public CachedAudioLoader(final AudioLoader alternativeAudioLoader, final Map<String, Clip> cache) {
        this.alternativeAudioLoader = alternativeAudioLoader;
        this.cache = cache;
    }

    @Override
    public Clip loadClipByName(final String name) {
        if (!cache.containsKey(name)) {
           cache.put(name, alternativeAudioLoader.loadClipByName(name));
        }

        return cache.get(name);
    }
}
