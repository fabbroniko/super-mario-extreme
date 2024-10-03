package com.fabbroniko.audio;

import com.fabbroniko.ul.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AudioClipCacheTest {

    private static final String NAME = "test-resource-name";

    @Mock
    private Map<String, Clip> cache;
    @Mock
    private Clip clip;
    @Mock
    private Logger logger;
    @InjectMocks
    private AudioClipCache audioClipCache;

    @Test
    void shouldGetValueFromCache() {
        audioClipCache.findByName(NAME);

        verify(cache).get(NAME);
    }

    @Test
    void shouldReturnEmptyOptional() {
        assertThat(audioClipCache.findByName(NAME)).isEmpty();
    }

    @Test
    void shouldReturnValueFromCache() {
        when(cache.get(anyString())).thenReturn(clip);

        assertThat(audioClipCache.findByName(NAME)).contains(clip);
    }

    @Test
    void shouldCheckCacheContainsResource() {
        audioClipCache.insert(NAME, clip);

        verify(cache).containsKey(NAME);
    }

    @Test
    void shouldInsertResource() {
        audioClipCache.insert(NAME, clip);

        verify(cache).put(NAME, clip);
    }

    @Test
    void shouldNotInsertAlreadyRegisteredResource() {
        when(cache.containsKey(anyString())).thenReturn(true);

        audioClipCache.insert(NAME, clip);

        verify(cache, never()).put(anyString(), any());
    }

    @Test
    void shouldLogAlreadyRegisteredResource() {
        when(cache.containsKey(anyString())).thenReturn(true);

        audioClipCache.insert(NAME, clip);

        verify(logger).error("insert_audio_clip_in_cache_already_registered", NAME);
    }


}
