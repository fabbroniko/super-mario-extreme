package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;

import javax.sound.sampled.Clip;

@Component
public final class EffectPlayerImpl implements EffectPlayer {

	private final AudioLoader audioLoader;
	private final SettingsProvider settingsProvider;

	public EffectPlayerImpl(final SettingsProvider settingsProvider, @Qualifier("cachedAudioLoader") final AudioLoader audioLoader) {
		this.settingsProvider = settingsProvider;
		this.audioLoader = audioLoader;
	}

	@Override
	public void play(final String name) {
		if (!settingsProvider.getSettings().isEffectsAudioActive()) {
			return; 
		}

		final Clip effect = audioLoader.loadClipByName(name);
		effect.stop();
		effect.setFramePosition(0);
		effect.start();
	}
}
