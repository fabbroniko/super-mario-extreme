package com.fabbroniko.resources;

import com.fabbroniko.resources.domain.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceManager {

    private static final String RESOURCE_DESCRIPTOR_PATH = "/resources.index";

    private final Resource resource;
    private Map<String, Clip> preLoadedAudioClips;

    public ResourceManager() {
        final ObjectMapper mapper = new XmlMapper();

        try {
            resource = mapper.readValue(getClass().getResource(RESOURCE_DESCRIPTOR_PATH), Resource.class);
        } catch (final IOException e) {
            // TODO below
            e.printStackTrace();
            throw new RuntimeException();
        }
        preLoadedAudioClips = new HashMap<>();
    }

    /**
     * This method preloads all resources with preload=true in memory for faster access
     */
    public void preload() {

    }

    public Clip findAudioClipFromName(final String name) {
        if(preLoadedAudioClips.containsKey(name)) {
            System.out.println("Loading audio " + name + " from cache.");
            return preLoadedAudioClips.get(name);
        }

        System.out.println("Loading audio " + name + " from disk.");
        return getAudioClipFromDisk(name);
    }

    private Clip getAudioClipFromDisk(final String name) {
        final Optional<com.fabbroniko.resources.domain.Clip> optResourceClip = resource.getAudio().getClip()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findFirst();

        if(optResourceClip.isEmpty())
            return null;

        try {
            final Clip clip = AudioSystem.getClip();
            final InputStream audioClipStream = getClass().getResourceAsStream(optResourceClip.get().getPath());
            final AudioInputStream ais = AudioSystem.getAudioInputStream(audioClipStream);

            clip.open(ais);
            return clip;
        } catch (final LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            // TODO
            e.printStackTrace();
            return null;
        }
    }
}
