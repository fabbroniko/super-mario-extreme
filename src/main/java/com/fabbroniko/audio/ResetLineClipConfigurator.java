package com.fabbroniko.audio;

import javax.sound.sampled.Clip;

public class ResetLineClipConfigurator implements ClipConfigurator {

    @Override
    public Clip configureLineListener(final Clip clip) {
        clip.addLineListener(new ResetClipLineListener());
        return clip;
    }
}
