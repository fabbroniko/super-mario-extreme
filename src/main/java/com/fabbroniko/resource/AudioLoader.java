package com.fabbroniko.resource;

import javax.sound.sampled.Clip;
import java.util.Optional;

public interface AudioLoader {

    Optional<Clip> findClipByName(final String name);
}
