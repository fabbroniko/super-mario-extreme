package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;

@Component
public final class DisabledEffectPlayer implements EffectPlayer {

	@Override
	public void play(final String name) {
	}
}
