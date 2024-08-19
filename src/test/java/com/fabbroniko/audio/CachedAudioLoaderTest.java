package com.fabbroniko.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachedAudioLoaderTest {

    private static final String RESOURCE_NAME = "test-resource-name";

    @Mock
    private AudioLoader alternativeAudioLoader;
    @Mock
    private Map<String, Clip> cache;
    @Mock
    private Clip clip;
    @InjectMocks
    private CachedAudioLoader cachedAudioLoader;

    @Test
    void shouldCheckIfKeyIsPresent() {
        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(cache).containsKey(RESOURCE_NAME);
    }

    @Test
    void shouldLoadClipFromAlternativeLoaderIfNotCached() {
        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(alternativeAudioLoader).loadClipByName(RESOURCE_NAME);
    }

    @Test
    void shouldPutClipIntoCache() {
        when(alternativeAudioLoader.loadClipByName(anyString())).thenReturn(clip);

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(cache).put(RESOURCE_NAME, clip);
    }

    @Test
    void shouldGetClipFromCache() {
        when(alternativeAudioLoader.loadClipByName(anyString())).thenReturn(clip);

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(cache).get(RESOURCE_NAME);
    }

    @Test
    void shouldReturnClipFromCache() {
        when(cache.get(anyString())).thenReturn(clip);

        cachedAudioLoader.loadClipByName(RESOURCE_NAME);

        assertThat(cachedAudioLoader.loadClipByName(RESOURCE_NAME))
                .isEqualTo(clip);
    }
}