package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;

@Component
public class SettingsBasedMusicPlayerProvider implements MusicPlayerProvider {

    private final MusicPlayer defaultMusicPlayer;
    private final MusicPlayer disabledMusicPlayer;
    private final SettingsProvider settingsProvider;

    public SettingsBasedMusicPlayerProvider(@Qualifier("defaultMusicPlayer") final MusicPlayer defaultMusicPlayer,
                                            @Qualifier("disabledMusicPlayer") final MusicPlayer disabledMusicPlayer,
                                            final SettingsProvider settingsProvider) {
        this.defaultMusicPlayer = defaultMusicPlayer;
        this.disabledMusicPlayer = disabledMusicPlayer;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public MusicPlayer getMusicPlayer() {
        if (settingsProvider.getSettings().isMusicActive()) {
            return defaultMusicPlayer;
        }

        return disabledMusicPlayer;
    }
}
