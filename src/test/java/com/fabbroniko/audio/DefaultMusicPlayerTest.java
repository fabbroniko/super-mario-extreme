package com.fabbroniko.audio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultMusicPlayerTest {

    private static final String NAME = "test-name";

    @Mock
    private Clip clip;
    @Mock
    private AudioLoader audioLoader;
    @InjectMocks
    private DefaultMusicPlayer defaultMusicPlayer;

    @BeforeEach
    void setUp() {
        when(audioLoader.loadClipByName(anyString())).thenReturn(clip);
    }

    @Test
    void shouldLoadClip() {
        defaultMusicPlayer.play(NAME, false);

        verify(audioLoader).loadClipByName(NAME);
    }

    @Test
    void shouldNotSetLoop() {
        defaultMusicPlayer.play(NAME, false);

        verify(clip, never()).loop(anyInt());
    }

    @Test
    void shouldStartMusic() {
        defaultMusicPlayer.play(NAME, false);

        verify(clip).start();
    }

    @Test
    void shouldSetLoop() {
        defaultMusicPlayer.play(NAME, true);

        verify(clip).loop(anyInt());
    }

    @Test
    void shouldStartLoopMusic() {
        defaultMusicPlayer.play(NAME, true);

        verify(clip).start();
    }

    @Test
    void shouldStopMusicBeforePlaying() {
        defaultMusicPlayer.play(NAME, false);
        defaultMusicPlayer.play(NAME, false);

        verify(clip).stop();
    }

    @Test
    void shouldResetFrameBeforePlaying() {
        defaultMusicPlayer.play(NAME, false);
        defaultMusicPlayer.play(NAME, false);

        verify(clip).setFramePosition(0);
    }

    @Test
    void shouldStopMusic() {
        defaultMusicPlayer.play(NAME, false);
        defaultMusicPlayer.stop();

        verify(clip).stop();
    }

    @Test
    void shouldResetFrame() {
        defaultMusicPlayer.play(NAME, false);
        defaultMusicPlayer.stop();

        verify(clip).setFramePosition(0);
    }
}