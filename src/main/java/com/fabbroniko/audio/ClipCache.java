package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;

import javax.sound.sampled.Clip;
import java.util.HashMap;

@Component
public class ClipCache extends HashMap<String, Clip> {
}
