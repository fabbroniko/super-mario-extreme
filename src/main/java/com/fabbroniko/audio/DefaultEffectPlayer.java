package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

import javax.sound.sampled.Clip;

@Component
public final class DefaultEffectPlayer implements EffectPlayer {

	private final AudioLoader audioLoader;

	public DefaultEffectPlayer(@Qualifier("cachedAudioLoader") final AudioLoader audioLoader) {
		this.audioLoader = audioLoader;
	}

	@Override
	public void play(final String name) {
		final Clip effect = audioLoader.loadClipByName(name);
		effect.stop();
		effect.setFramePosition(0);
		effect.start();
	}
}
