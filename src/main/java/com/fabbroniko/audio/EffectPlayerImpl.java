package com.fabbroniko.audio;

import com.fabbroniko.settings.SettingsProvider;

import javax.sound.sampled.Clip;

public final class EffectPlayerImpl implements EffectPlayer {

	private final AudioLoader audioLoader;
	private final SettingsProvider settingsProvider;

	public EffectPlayerImpl(final SettingsProvider settingsProvider, final AudioLoader audioLoader) {
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
