package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

import javax.sound.sampled.Clip;

@Component
public final class DefaultMusicPlayer implements MusicPlayer {

	private Clip music;
	private final AudioLoader audioLoader;

	public DefaultMusicPlayer(@Qualifier("cachedAudioLoader") final AudioLoader audioLoader) {
		this.audioLoader = audioLoader;
	}

	@Override
	public void play(final String name, final boolean loop) {
		stop();

		music = audioLoader.loadClipByName(name);
		if(loop) {
			music.loop(Clip.LOOP_CONTINUOUSLY);
		}

		music.start();
	}

	@Override
	public void stop() {
		if (music != null) {
			music.stop();
			music.setFramePosition(0);
		}
	}
}
