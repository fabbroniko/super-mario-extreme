package com.fabbroniko.audio;

import com.fabbroniko.resource.dto.SettingsDto;
import com.fabbroniko.settings.SettingsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsBasedMusicPlayerProviderTest {

    @Mock
    private MusicPlayer defaultMusicPlayer;
    @Mock
    private MusicPlayer disabledMusicPlayer;
    @Mock
    private SettingsProvider settingsProvider;
    @Mock
    private SettingsDto settings;

    private SettingsBasedMusicPlayerProvider musicPlayerProvider;

    @BeforeEach
    void setUp() {
        when(settingsProvider.getSettings()).thenReturn(settings);

        musicPlayerProvider = new SettingsBasedMusicPlayerProvider(
            defaultMusicPlayer,
            disabledMusicPlayer,
            settingsProvider);
    }

    @Test
    void shouldCheckMusicIsActive() {
        musicPlayerProvider.getMusicPlayer();

        verify(settings).isMusicActive();
    }

    @Test
    void shouldReturnDisabledMusicPlayer() {
        assertThat(musicPlayerProvider.getMusicPlayer())
            .isEqualTo(disabledMusicPlayer);
    }

    @Test
    void shouldReturnActiveMusicPlayer() {
        when(settings.isMusicActive()).thenReturn(true);

        assertThat(musicPlayerProvider.getMusicPlayer())
            .isEqualTo(defaultMusicPlayer);
    }
}