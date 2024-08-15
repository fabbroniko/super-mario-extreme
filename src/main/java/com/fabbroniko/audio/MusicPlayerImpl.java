package com.fabbroniko.audio;

import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.resource.AudioLoader;

import javax.sound.sampled.Clip;

public final class MusicPlayerImpl implements MusicPlayer {

	private Clip music;
	private final AudioLoader audioLoader;
	private final SettingsProvider settingsProvider;

	public MusicPlayerImpl(final SettingsProvider settingsProvider, final AudioLoader audioLoader) {
		this.settingsProvider = settingsProvider;
		this.audioLoader = audioLoader;
	}

	@Override
	public void play(final String name, final boolean loop) {
		if (!settingsProvider.getSettings().isMusicActive()) {
			return;
		}

		stop();

		music = audioLoader.findClipByName(name);
		if(loop) {
			music.loop(Clip.LOOP_CONTINUOUSLY);
		}

		music.start();
	}

	@Override
	public void stop() {
		if (music != null) {
			music.stop();
		}
	}
}
