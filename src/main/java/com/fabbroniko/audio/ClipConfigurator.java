package com.fabbroniko.audio;

import javax.sound.sampled.Clip;

public interface ClipConfigurator {

    Clip configureLineListener(final Clip clip);
}
