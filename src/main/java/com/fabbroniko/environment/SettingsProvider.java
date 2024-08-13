package com.fabbroniko.environment;

import com.fabbroniko.resource.dto.Settings;

public interface SettingsProvider {

    Settings getSettings();

    void saveSettings();
}
