package com.fabbroniko.environment;

import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Settings;

public class SettingsProviderImpl implements SettingsProvider {

    private final ResourceManager resourceManager;

    private Settings settings;

    public SettingsProviderImpl(final ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public Settings getSettings() {
        if(settings == null) {
            settings = resourceManager.loadSettings();
        }

        return settings;
    }

    @Override
    public void saveSettings() {
        resourceManager.saveSettings(settings);
    }
}
