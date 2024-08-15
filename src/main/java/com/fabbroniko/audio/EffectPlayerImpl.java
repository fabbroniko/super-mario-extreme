package com.fabbroniko.audio;

import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.resource.AudioLoader;

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

		final Clip effect = audioLoader.findClipByName(name);
		effect.start();
	}
}
