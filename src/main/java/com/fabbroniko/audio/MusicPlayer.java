package com.fabbroniko.audio;

public interface MusicPlayer {

    void playBackgroundMusic(final String clipName, final boolean loop);

    void stopMusic();
}
