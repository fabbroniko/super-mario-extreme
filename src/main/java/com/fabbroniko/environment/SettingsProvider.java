package com.fabbroniko.environment;

import com.fabbroniko.resource.dto.SettingsDto;

public interface SettingsProvider {

    SettingsDto getSettings();

    void saveSettings();
}
