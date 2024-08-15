package com.fabbroniko.audio;

public interface MusicPlayer {

    void play(final String name, final boolean loop);

    void stop();
}
