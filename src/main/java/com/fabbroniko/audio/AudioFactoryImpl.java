package com.fabbroniko.audio;

import lombok.SneakyThrows;
import org.example.annotation.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

@Component
public class AudioFactoryImpl implements AudioFactory {

    @SneakyThrows
    @Override
    public Clip createClip() {
        return AudioSystem.getClip();
    }

    @SneakyThrows
    @Override
    public AudioInputStream createAudioInputStream(final InputStream inputStream) {
        final InputStream bufferedInputStream = new BufferedInputStream(inputStream);
        return AudioSystem.getAudioInputStream(bufferedInputStream);
    }
}
