package com.fabbroniko.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ResetClipLineListener implements LineListener {

    @Override
    public void update(final LineEvent event) {
        if (LineEvent.Type.STOP.equals(event.getType())) {
            final Clip source = (Clip) event.getSource();
            source.setFramePosition(0);
        }
    }
}
