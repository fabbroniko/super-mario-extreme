package com.fabbroniko.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public interface AudioFactory {

    Clip createClip();

    AudioInputStream createAudioInputStream(final InputStream inputStream);
}
