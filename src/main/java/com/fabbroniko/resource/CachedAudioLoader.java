package com.fabbroniko.resource;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;

public class CachedAudioLoader implements AudioLoader {

    private final Map<String, Clip> cache;
    private final AudioLoader alternateSource;

    public CachedAudioLoader(final AudioLoader alternateSource) {
        this.alternateSource = alternateSource;
        this.cache = new HashMap<>();
    }

    @Override
    public Clip findClipByName(final String name) {
        if (!cache.containsKey(name)) {
           cache.put(name, alternateSource.findClipByName(name));
        }

        final Clip clip = cache.get(name);
        clip.stop();
        clip.setFramePosition(0);

        return clip;
    }
}
