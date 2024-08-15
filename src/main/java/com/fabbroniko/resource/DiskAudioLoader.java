package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Resource;
import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import java.io.InputStream;
import java.util.Optional;

@Log4j2
public class DiskAudioLoader implements AudioLoader {

    private final Resource resource;
    private final LineListener lineListener;

    public DiskAudioLoader(final Resource resource, final LineListener lineListener) {
        this.resource = resource;
        this.lineListener = lineListener;
    }

    @Override
    public Optional<Clip> findClipByName(String name) {
        final Optional<Clip> optClip = loadAudioClipFromDisk(name);
        optClip.ifPresent(clip -> clip.addLineListener(lineListener));

        return optClip;
    }

    private Optional<Clip> loadAudioClipFromDisk(final String name) {
        final Optional<com.fabbroniko.resource.dto.Clip> optResourceClip = resource.getClips()
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

            retValue = Optional.of(clip);
        } catch (final Exception e) {
            log.error("An exception occurred while loading audio clip {}. Exception message {}", name, e.getMessage());
        }

        return retValue;
    }
}
