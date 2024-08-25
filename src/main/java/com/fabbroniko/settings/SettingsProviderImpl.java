package com.fabbroniko.settings;

import com.fabbroniko.resource.dto.SettingsDto;

public class SettingsProviderImpl implements SettingsProvider {

    private final UserSettingsLoader userSettingsLoader;

    private SettingsDto settings;

    public SettingsProviderImpl(final UserSettingsLoader userSettingsLoader) {
        this.userSettingsLoader = userSettingsLoader;
    }

    @Override
    public SettingsDto getSettings() {
        if(settings == null) {
            settings = userSettingsLoader.load();
        }

        return settings;
    }

    @Override
    public void saveSettings() {
        userSettingsLoader.save(settings);
    }
}
