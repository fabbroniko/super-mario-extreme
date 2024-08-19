package com.fabbroniko.audio;

import javax.sound.sampled.Clip;

public interface AudioLoader {

    Clip loadClipByName(final String name);
}
