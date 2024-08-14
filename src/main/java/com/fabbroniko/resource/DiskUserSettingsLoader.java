package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Log4j2
public class DiskUserSettingsLoader implements UserSettingsLoader {

    private static final String SUPER_MARIO_USER_HOME_DIR = System.getProperty("user.home") + File.separator + "super-mario-extreme" + File.separator;
    private static final String SETTINGS_FILE_NAME = "settings.json";

    private final ObjectMapper objectMapper;

    public DiskUserSettingsLoader(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Settings load() {
        try (FileInputStream fis = new FileInputStream(SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME)) {
            return objectMapper.readValue(fis, Settings.class);
        } catch (final IOException e) {
            log.error("Unable to load settings from the user home directory {}. Exception {}", SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME, e.getMessage());
        }

        return new Settings();
    }

    public void save(final Settings settings) {
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