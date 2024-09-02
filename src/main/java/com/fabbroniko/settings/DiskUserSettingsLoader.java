package com.fabbroniko.settings;

import com.fabbroniko.resource.dto.SettingsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Supplier;

@Log4j2
public class DiskUserSettingsLoader implements UserSettingsLoader {

    private static final String SUPER_MARIO_USER_HOME_DIR = System.getProperty("user.home") + File.separator + "super-mario-extreme" + File.separator;
    private static final String SETTINGS_FILE_NAME = "settings.json";

    private final ObjectMapper objectMapper;

    public DiskUserSettingsLoader(final Supplier<ObjectMapper> jsonObjectMapper) {
        this.objectMapper = jsonObjectMapper.get();
    }

    public SettingsDto load() {
        try (FileInputStream fis = new FileInputStream(SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME)) {
            return objectMapper.readValue(fis, SettingsDto.class);
        } catch (final IOException e) {
            log.error("Unable to load settings from the user home directory {}. Exception {}", SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME, e.getMessage());
        }

        return new SettingsDto();
    }

    public void save(final SettingsDto settings) {
        final File homeDirectory = new File(SUPER_MARIO_USER_HOME_DIR);
        final File settingsFile = new File(homeDirectory, SETTINGS_FILE_NAME);

        try {
            if(homeDirectory.mkdirs() || settingsFile.createNewFile()) {
                log.info("Settings file doesn't exist, a new one will be created.");
            } else {
                log.info("The existing settings file will be overridden.");
            }

            objectMapper.writeValue(settingsFile, settings);
        } catch (final IOException e) {
            log.error("Unable to save settings to the users home directory {}. Exception {}", SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME, e.getMessage());
        }
    }
}
