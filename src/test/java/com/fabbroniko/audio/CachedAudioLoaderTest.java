package com.fabbroniko.audio;

import com.fabbroniko.resource.ResourceCache;
import com.fabbroniko.ul.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachedAudioLoaderTest {

    private static final String RESOURCE_NAME = "test-resource-name";

    @Mock
    private AudioLoader alternativeAudioLoader;
    @Mock
    private ResourceCache<Clip> cache;
    @Mock
    private Clip clip;
    @Mock
    private Logger logger;
    @InjectMocks
    private CachedAudioLoader cachedAudioLoader;

    @Test
    void shouldLookupAudioClipInCache() {
        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(cache).findByName(RESOURCE_NAME);
    }

    @Test
    void shouldLoadClipFromAlternativeLoaderIfNotCached() {
        when(cache.findByName(anyString())).thenReturn(Optional.empty());

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(alternativeAudioLoader).loadClipByName(RESOURCE_NAME);
    }

    @Test
    void shouldLogCacheMiss() {
        when(cache.findByName(anyString())).thenReturn(Optional.empty());
        when(alternativeAudioLoader.loadClipByName(anyString())).thenReturn(clip);

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(logger).info("audio_clip_cache_miss", RESOURCE_NAME);
    }

    @Test
    void shouldPutClipIntoCache() {
        when(cache.findByName(anyString())).thenReturn(Optional.empty());
        when(alternativeAudioLoader.loadClipByName(anyString())).thenReturn(clip);

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(cache).insert(RESOURCE_NAME, clip);
    }

    @Test
    void shouldReturnValueFromAlternateLoader() {
        when(cache.findByName(anyString())).thenReturn(Optional.empty());
        when(alternativeAudioLoader.loadClipByName(anyString())).thenReturn(clip);

        assertThat(cachedAudioLoader.loadClipByName(RESOURCE_NAME))
            .isEqualTo(clip);
    }

    @Test
    void shouldNotDelegateAlternateLoader() {
        when(cache.findByName(anyString())).thenReturn(Optional.of(clip));

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(alternativeAudioLoader, never()).loadClipByName(anyString());
    }

    @Test
    void shouldLogCacheHit() {
        when(cache.findByName(anyString())).thenReturn(Optional.of(clip));

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(logger).trace("audio_clip_cache_hit", RESOURCE_NAME);
    }

    @Test
    void shouldReturnClipFromCache() {
        when(cache.findByName(anyString())).thenReturn(Optional.of(clip));

        assertThat(cachedAudioLoader.loadClipByName(RESOURCE_NAME))
                .isEqualTo(clip);
    }
}