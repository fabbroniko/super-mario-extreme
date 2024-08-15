package com.fabbroniko.audio.line;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ResetClipLineListener implements LineListener {

    @Override
    public void update(final LineEvent event) {
        if (event.getType().equals(LineEvent.Type.STOP)) {
            final Clip source = (Clip) event.getSource();
            source.stop();
            source.setFramePosition(0);
        }
    }
}
