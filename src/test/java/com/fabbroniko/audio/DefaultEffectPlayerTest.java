package com.fabbroniko.audio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEffectPlayerTest {

    private static final String NAME = "test-name";

    @Mock
    private AudioLoader audioLoader;
    @Mock
    private Clip clip;
    @InjectMocks
    private DefaultEffectPlayer defaultEffectPlayer;

    @BeforeEach
    void setUp() {
        when(audioLoader.loadClipByName(anyString())).thenReturn(clip);
    }

    @Test
    void shouldLoadClipByName() {
        defaultEffectPlayer.play(NAME);

        verify(audioLoader).loadClipByName(NAME);
    }

    @Test
    void shouldStopClip() {
        defaultEffectPlayer.play(NAME);

        verify(clip).stop();
    }

    @Test
    void shouldResetFramePosition() {
        defaultEffectPlayer.play(NAME);

        verify(clip).setFramePosition(0);
    }

    @Test
    void shouldStartClip() {
        defaultEffectPlayer.play(NAME);

        verify(clip).start();
    }
}