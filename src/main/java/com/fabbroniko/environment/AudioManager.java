package com.fabbroniko.environment;

import javax.sound.sampled.Clip;

import com.fabbroniko.gamestatemanager.GameManager;
import com.fabbroniko.resources.ResourceManager;

public final class AudioManager {
	
	private Clip music;
	private final ResourceManager resourceManager;
	private final GameManager gameManager;

	public AudioManager(final GameManager gameManager, final ResourceManager resourceManager) {
		this.gameManager = gameManager;
		this.resourceManager = resourceManager;
	}

	public void playBackgroundMusic(final String clipName, final boolean loop) {
		if (!gameManager.getSettings().isMusicActive()) {
			return;
		}

		final Clip clip = resourceManager.findAudioClipFromName(clipName);
		if(clip == null)
			return;

		if(loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		/*
		clip.addLineListener(event -> {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				((Clip)event.getSource()).close();
			}
		});
		*/

		music = clip;
		music.start();
	}
	
	public void playEffect(final String clipName) {
		if (!gameManager.getSettings().isEffectsAudioActive()) {
			return; 
		}

		final Clip clip = resourceManager.findAudioClipFromName(clipName);
		if(clip == null)
			return;

		/*
		clip.addLineListener(event -> {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				((Clip)event.getSource()).close();
			}
		});
		*/

		clip.start();
	}

	public void stopCurrent() {
		if (music != null) {
			music.stop();
		}
	}
}
