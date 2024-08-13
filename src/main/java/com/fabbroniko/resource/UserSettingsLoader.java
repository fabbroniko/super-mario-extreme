package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Settings;

public interface UserSettingsLoader {

    Settings load();

    void save(final Settings settings);
}
