package com.fabbroniko.audio;

import com.fabbroniko.sdi.annotation.Component;

@Component
public final class DisabledMusicPlayer implements MusicPlayer {

	@Override
	public void play(final String name, final boolean loop) {
	}

	@Override
	public void stop() {
	}
}
