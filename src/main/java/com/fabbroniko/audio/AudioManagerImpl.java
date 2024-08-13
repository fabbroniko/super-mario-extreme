package com.fabbroniko.audio;

import com.fabbroniko.main.SettingsProvider;
import com.fabbroniko.resource.ResourceManager;

import javax.sound.sampled.Clip;

public final class AudioManagerImpl implements AudioManager {

	private Clip music;
	private final ResourceManager resourceManager;
	private final SettingsProvider settingsProvider;

	public AudioManagerImpl(final SettingsProvider settingsProvider, final ResourceManager resourceManager) {
		this.settingsProvider = settingsProvider;
		this.resourceManager = resourceManager;
	}

	@Override
	public void playBackgroundMusic(final String clipName, final boolean loop) {
		if (!settingsProvider.getSettings().isMusicActive()) {
			return;
		}

		// Making sure there is only one music clip active at a time
		stopMusic();

		resourceManager.findAudioClipFromName(clipName).ifPresent(c -> {
			if(loop)
				c.loop(Clip.LOOP_CONTINUOUSLY);

			music = c;
			c.start();
		});
	}

	@Override
	public void playEffect(final String clipName) {
		if (!settingsProvider.getSettings().isEffectsAudioActive()) {
			return; 
		}

		resourceManager.findAudioClipFromName(clipName).ifPresent(Clip::start);
	}

	@Override
	public void stopMusic() {
		if (music != null) {
			music.stop();
		}
	}
}
