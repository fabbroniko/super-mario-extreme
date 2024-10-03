package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;

@Component
public class SettingsBasedEffectPlayerProvider implements EffectPlayerProvider {

    private final EffectPlayer defaultEffectPlayer;
    private final EffectPlayer disabledEffectPlayer;
    private final SettingsProvider settingsProvider;

    public SettingsBasedEffectPlayerProvider(@Qualifier("defaultEffectPlayer") final EffectPlayer defaultEffectPlayer,
                                             @Qualifier("disabledEffectPlayer") final EffectPlayer disabledEffectPlayer,
                                             final SettingsProvider settingsProvider) {
        this.defaultEffectPlayer = defaultEffectPlayer;
        this.disabledEffectPlayer = disabledEffectPlayer;
        this.settingsProvider = settingsProvider;
    }

    @Override
    public EffectPlayer getEffectPlayer() {
        if (settingsProvider.getSettings().isEffectsAudioActive()) {
            return defaultEffectPlayer;
        }

        return disabledEffectPlayer;
    }
}
