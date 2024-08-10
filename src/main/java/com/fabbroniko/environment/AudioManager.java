package com.fabbroniko.environment;

public interface AudioManager {

	void playBackgroundMusic(final String clipName, final boolean loop);

	void playEffect(final String clipName);

	public void stopMusic();
}
