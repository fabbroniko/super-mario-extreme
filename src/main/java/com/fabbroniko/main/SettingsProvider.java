package com.fabbroniko.main;

import com.fabbroniko.resource.domain.Settings;

public interface SettingsProvider {

    Settings getSettings();

    void saveSettings();
}
