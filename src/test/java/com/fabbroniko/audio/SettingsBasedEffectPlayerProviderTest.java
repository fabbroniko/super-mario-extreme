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
class SettingsBasedEffectPlayerProviderTest {

    @Mock
    private EffectPlayer defaultEffectPlayer;
    @Mock
    private EffectPlayer disabledEffectPlayer;
    @Mock
    private SettingsProvider settingsProvider;
    @Mock
    private SettingsDto settings;

    private SettingsBasedEffectPlayerProvider effectPlayerProvider;

    @BeforeEach
    void setUp() {
        when(settingsProvider.getSettings()).thenReturn(settings);

        effectPlayerProvider = new SettingsBasedEffectPlayerProvider(
            defaultEffectPlayer,
            disabledEffectPlayer,
            settingsProvider);
    }

    @Test
    void shouldCheckMusicIsActive() {
        effectPlayerProvider.getEffectPlayer();

        verify(settings).isEffectsAudioActive();
    }

    @Test
    void shouldReturnDisabledMusicPlayer() {
        assertThat(effectPlayerProvider.getEffectPlayer())
            .isEqualTo(disabledEffectPlayer);
    }

    @Test
    void shouldReturnActiveMusicPlayer() {
        when(settings.isEffectsAudioActive()).thenReturn(true);

        assertThat(effectPlayerProvider.getEffectPlayer())
            .isEqualTo(defaultEffectPlayer);
    }
}