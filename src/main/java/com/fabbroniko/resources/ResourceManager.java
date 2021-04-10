package com.fabbroniko.resources;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.resources.domain.Background;
import com.fabbroniko.resources.domain.Resource;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The resource manager takes care of referencing, loading and caching resources such as audio clips, sprites and tilemap
 *
 * It uses a resource index as list of resources to load and what options are available to them.
 */
@Log4j2
public class ResourceManager {

    private static final String RESOURCE_DESCRIPTOR_PATH = "/resources.index";

    private Resource resource;
    private final Map<String, Clip> preLoadedAudioClips;
    private final Map<String, BufferedImage> preLoadedImages;

    public ResourceManager() {
        preLoadedAudioClips = new HashMap<>();
        preLoadedImages = new HashMap<>();
    }

    /**
     * This method initializes the resource manager and preloads all resources with preload=true in memory for faster access.
     * Concurrent access of this method should not be allowed
     */
    public synchronized void preload() {
        if(resource != null)
            return;

        try {
            // Using jackson to deserialize the resource index into its object representation
            resource = new XmlMapper().readValue(getClass().getResource(RESOURCE_DESCRIPTOR_PATH), Resource.class);
        } catch (final IOException e) {
            log.fatal("Unable to initialize the resource manager. {}", e.getMessage());
            throw new RuntimeException(); // TODO handle this properly once error management is refactored
        }

        log.info("Pre-loading audio clips from disk.");

        resource.getClips().forEach(clip -> {
            if(clip.isPreload()) {
                loadAudioClipFromDisk(clip.getName()).ifPresent(c -> preLoadedAudioClips.put(clip.getName(), c));
            }
        });

        preLoadedImages.putAll(resource.getBackgrounds()
                .stream()
                .collect(Collectors.toMap(Background::getName, b -> findBackgroundFromName(b.getName())))
        );
    }

    /**
     * Finds an audio clip from the name passed as parameter. This method looks for a match in the pre-loaded cache if
     * available, otherwise it's loaded from disk.
     *
     * @param name The unique identifier for the resource found in the resource.index file
     * @return An empty Optional if there was a problem retrieving the resource or the fully set up clip if it was successfully loaded
     */
    public Optional<Clip> findAudioClipFromName(final String name) {
        // Attempts to retrieve the value from the pre-loaded cache first
        if(preLoadedAudioClips.containsKey(name)) {
            final Clip clip = preLoadedAudioClips.get(name);

            // Make sure the Frame is set back to the beginning of the clip.
            // This allows reuse of previously constructed objects.
            clip.stop();
            clip.setFramePosition(0);

            log.info("Fetched clip named {} from cache", name);
            return Optional.of(clip);
        }

        // If it's not preloaded we need to load it from disk and make sure the clip is closed once stopped to avoid memory leaks
        final Optional<Clip> optClip = loadAudioClipFromDisk(name);
        optClip.ifPresent(clip -> clip.addLineListener(event -> {
            if (event.getType().equals(LineEvent.Type.STOP)) {
                ((Clip) event.getSource()).close();
            }
        }));

        return optClip;
    }

    /**
     * Loads the audio clip from the disk.
     *
     * @param name Unique identifier specified in the resource.index file.
     * @return The audio clip ready to be played.
     */
    private Optional<Clip> loadAudioClipFromDisk(final String name) {
        log.trace("Loading audio clip from disk. Clip name {}", name);

        // Find the clip descriptor from the resource index
        final Optional<com.fabbroniko.resources.domain.Clip> optResourceClip = resource.getClips()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny();

        Optional<Clip> retValue = Optional.empty();
        if(optResourceClip.isEmpty()) {
            log.error("Couldn't find audio clip with name {} in the resource index.", name);
            return retValue;
        }

        try {
            final Clip clip = AudioSystem.getClip();
            final InputStream audioClipStream = getClass().getResourceAsStream(optResourceClip.get().getPath());
            if(audioClipStream == null) {
                log.error("Couldn't find audio clip {} in classpath with path {}", name, optResourceClip.get().getPath());
                return retValue;
            }

            final AudioInputStream ais = AudioSystem.getAudioInputStream(audioClipStream);

            clip.open(ais);

            log.info("Successfully loaded audio clip {} from disk.", name);
            retValue = Optional.of(clip);
        } catch (final Exception e) {
            log.error("An exception occurred while loading audio clip {}. Exception message {}", name, e.getMessage());
        }

        return retValue;
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

    private BufferedImage loadImageFromDisk(final String path) {
        log.trace("Loading image with path {} from disk.", path);

        try (final InputStream imageInputStream = getClass().getResourceAsStream(path)) {
            return ImageIO.read(imageInputStream);
        } catch (Exception e) {
            log.error("Unable to load image {} from disk. Exception {}", path, e.getMessage());
            throw new ResourceNotFoundException(path);
        }
    }
}
