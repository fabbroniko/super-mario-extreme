package com.fabbroniko.environment;

import com.fabbroniko.sdi.annotation.Component;

import javax.sound.sampled.Clip;
import java.util.HashMap;

@Component
public class Temp extends HashMap<String, Clip> {
}
