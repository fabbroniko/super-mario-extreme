package com.fabbroniko.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class CloseClipLineListener implements LineListener {

    @Override
    public void update(final LineEvent event) {
        if (event.getType().equals(LineEvent.Type.STOP)) {
            final Clip source = (Clip) event.getSource();
            source.close();
        }
    }
}
