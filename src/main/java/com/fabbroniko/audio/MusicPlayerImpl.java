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
	public void playBackgroundMusic(final String clipName, final boolean loop) {
		if (!settingsProvider.getSettings().isMusicActive()) {
			return;
		}

		stopMusic();

		audioLoader.findClipByName(clipName).ifPresent(c -> {
			if(loop)
				c.loop(Clip.LOOP_CONTINUOUSLY);

			music = c;
			c.start();
		});
	}

	@Override
	public void stopMusic() {
		if (music != null) {
			music.stop();
		}
	}
}
