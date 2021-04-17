package com.fabbroniko.environment;

import javax.sound.sampled.Clip;

import com.fabbroniko.gamestatemanager.GameManager;
import com.fabbroniko.resource.ResourceManager;

import java.util.Optional;

public final class AudioManager {
	
	private Clip music;
	private final ResourceManager resourceManager;
	private final GameManager gameManager;

	/**
	 * Construct a new AudioManager that allows client classes to play music or sound effects with very simple method calls.
	 * @param gameManager The reference to the unique GameManager.
	 * @param resourceManager The reference to the ResourceManager that manages all the resources.
	 */
	public AudioManager(final GameManager gameManager, final ResourceManager resourceManager) {
		this.gameManager = gameManager;
		this.resourceManager = resourceManager;
	}

	/**
	 * Plays a background music.
	 * There are a few differences between sound effects and music. First of all they can be activated or disabled
	 * separately in the settings menu; background music can also be set to loop when it's over and lastly, music needs
	 * to be stopped when the scene changes while effects are normally so short that is not worth taking into account
	 * while swapping scene.
	 * @param clipName The name of the clip as seen in the resource index.
	 * @param loop Whether the music should restart when it reaches the end.
	 */
	public void playBackgroundMusic(final String clipName, final boolean loop) {
		if (!gameManager.getSettings().isMusicActive()) {
			return;
		}

		// Making sure there is only one music clip active at a time
		stopCurrent();

		resourceManager.findAudioClipFromName(clipName).ifPresent(c -> {
			if(loop)
				c.loop(Clip.LOOP_CONTINUOUSLY);

			music = c;
			c.start();
		});
	}

	/**
	 * Plays an audio effect.
	 * @param clipName The name of the clip to play
	 */
	public void playEffect(final String clipName) {
		if (!gameManager.getSettings().isEffectsAudioActive()) {
			return; 
		}

		resourceManager.findAudioClipFromName(clipName).ifPresent(Clip::start);
	}

	/**
	 * Stops the playback of the background music.
	 */
	public void stopCurrent() {
		Optional.ofNullable(music).ifPresent(Clip::stop);
	}
}
