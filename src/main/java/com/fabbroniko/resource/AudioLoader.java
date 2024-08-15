package com.fabbroniko.resource;

import javax.sound.sampled.Clip;

public interface AudioLoader {

    Clip findClipByName(final String name);
}
