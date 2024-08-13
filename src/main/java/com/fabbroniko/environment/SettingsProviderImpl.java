package com.fabbroniko.environment;

import com.fabbroniko.resource.UserSettingsLoader;
import com.fabbroniko.resource.dto.Settings;

public class SettingsProviderImpl implements SettingsProvider {

    private final UserSettingsLoader userSettingsLoader;

    private Settings settings;

    public SettingsProviderImpl(final UserSettingsLoader userSettingsLoader) {
        this.userSettingsLoader = userSettingsLoader;
    }

    @Override
    public Settings getSettings() {
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
