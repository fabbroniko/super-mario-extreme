package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.SettingsDto;

public interface UserSettingsLoader {

    SettingsDto load();

    void save(final SettingsDto settings);
}
