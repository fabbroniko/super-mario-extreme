package com.fabbroniko.audio;

import com.fabbroniko.settings.SettingsProvider;
import org.example.annotation.Component;
import org.example.annotation.Qualifier;

import javax.sound.sampled.Clip;

@Component
public final class MusicPlayerImpl implements MusicPlayer {

	private Clip music;
	private final AudioLoader audioLoader;
	private final SettingsProvider settingsProvider;

	public MusicPlayerImpl(final SettingsProvider settingsProvider, @Qualifier("cachedAudioLoader") final AudioLoader audioLoader) {
		this.settingsProvider = settingsProvider;
		this.audioLoader = audioLoader;
	}

	@Override
	public void play(final String name, final boolean loop) {
		if (!settingsProvider.getSettings().isMusicActive()) {
			return;
		}

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
