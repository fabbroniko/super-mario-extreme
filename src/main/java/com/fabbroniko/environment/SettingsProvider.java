package com.fabbroniko.environment;

import com.fabbroniko.resource.domain.Settings;

public interface SettingsProvider {

    Settings getSettings();

    void saveSettings();
}
