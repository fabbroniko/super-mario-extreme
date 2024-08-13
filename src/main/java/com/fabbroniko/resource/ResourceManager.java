package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.resource.dto.Background;
import com.fabbroniko.resource.dto.Clip;
import com.fabbroniko.resource.dto.Resource;
import com.fabbroniko.resource.dto.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class ResourceManager {

    private static final String RESOURCE_DESCRIPTOR_PATH = "/resources.index";
    private static final String SUPER_MARIO_USER_HOME_DIR = System.getProperty("user.home") + File.separator + "super-mario-extreme" + File.separator;
    private static final String SETTINGS_FILE_NAME = "settings.json";
    private static final ObjectMapper om = new ObjectMapper();

    private Resource resource;
    private final Map<String, BufferedImage> preLoadedImages;

    public ResourceManager() {
        preLoadedImages = new HashMap<>();
    }

    public synchronized void preload() {
        if(resource != null)
            return;

        try {
            // Using jackson to deserialize the resource index into its object representation
            resource = new XmlMapper().readValue(getClass().getResource(RESOURCE_DESCRIPTOR_PATH), Resource.class);
        } catch (final IOException e) {
            log.fatal("Unable to initialize the resource manager. {}", e.getMessage());
            throw new RuntimeException();
        }

        log.info("Pre-loading audio clips from disk.");

        preLoadedImages.putAll(resource.getBackgrounds()
                .stream()
                .collect(Collectors.toMap(Background::getName, b -> findBackgroundFromName(b.getName())))
        );
    }

    public BufferedImage findBackgroundFromName(final String name) {
        if(preLoadedImages.containsKey(name)) {
            log.trace("Getting background with name {} from pre-loaded cache.", name);
            return preLoadedImages.get(name);
        }

        log.trace("Loading background image {} from disk.", name);
        final Background background = resource.getBackgrounds()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException(name));

        return loadImageFromDisk(background.getPath());
    }

    public BufferedImage getTileMapSet() {
        log.trace("Loading tile map set (uncut).");

        return loadImageFromDisk(resource.getTilemap().getPath());
    }

    public BufferedImage loadImageFromDisk(final String path) {
        log.trace("Loading image with path {} from disk.", path);

        try (final InputStream imageInputStream = getClass().getResourceAsStream(path)) {
            return ImageIO.read(imageInputStream);
        } catch (Exception e) {
            log.error("Unable to load image {} from disk. Exception {}", path, e.getMessage());
            throw new ResourceNotFoundException(path);
        }
    }

    public Settings loadSettings() {
        try (FileInputStream fis = new FileInputStream(SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME)) {
            return om.readValue(fis, Settings.class);
        } catch (final IOException e) {
            log.error("Unable to load settings from the user home directory {}. Exception {}", SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME, e.getMessage());
        }

        return new Settings();
    }

    public void saveSettings(final Settings settings) {
        final File homeDirectory = new File(SUPER_MARIO_USER_HOME_DIR);
        final File settingsFile = new File(homeDirectory, SETTINGS_FILE_NAME);

        try {
            if(homeDirectory.mkdirs() || settingsFile.createNewFile()) {
                log.info("Settings file doesn't exist, a new one will be created.");
            } else {
                log.info("The existing settings file will be overridden.");
            }

            om.writeValue(settingsFile, settings);
        } catch (final IOException e) {
            log.error("Unable to save settings to the users home directory {}. Exception {}", SUPER_MARIO_USER_HOME_DIR + SETTINGS_FILE_NAME, e.getMessage());
        }
    }

    public List<Clip> getClips() {
        return resource.getClips();
    }
}
