package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.dto.Resource;
import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

@Log4j2
public class DiskAudioLoader implements AudioLoader {

    private final Resource resource;

    public DiskAudioLoader(final Resource resource) {
        this.resource = resource;
    }

    @Override
    public Clip findClipByName(String name) {
        final String path = resource.getClips()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new UndefinedResourceException(name, ResourceType.AUDIO))
                .getPath();

        try {
            final Clip clip = AudioSystem.getClip();
            final InputStream audioClipStream = getClass().getResourceAsStream(path);
            if(audioClipStream == null) {
                throw new ResourceNotFoundException(path); // TODO other exception maybe and pass over the originating exception
            }

            final AudioInputStream ais = AudioSystem.getAudioInputStream(audioClipStream);

            clip.open(ais);

           return clip;
        } catch (final Exception e) {
            throw new ResourceNotFoundException(path); // TODO other exception maybe and pass over the originating exception
        }
    }
}
