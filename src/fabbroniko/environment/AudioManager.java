package fabbroniko.environment;

import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import fabbroniko.error.ErrorManager;
import fabbroniko.gamestatemanager.GameManager;
import fabbroniko.resources.ResourceManager;
import fabbroniko.resources.ResourceService;
import fabbroniko.resources.Sound;

public final class AudioManager {
	
	private final Music myMusic;
	
	private static final AudioManager MY_INSTANCE = new AudioManager();
	private static final String ERROR_STOPPING_MUSIC = "Something went wrong trying to stop the actual running music.";
	private static final String ERROR_STARTING_MUSIC = "Something went wrong trying to start a new Sound.";
	private static final String ERROR_INITIALIZING_MUSIC = "Something went wrong trying to initialize the music instance.";
	
	private AudioManager() {
		myMusic = new Music();
		final Map<String, String> res = ResourceManager.getInstance().getResourcesFromTag(ResourceService.AUDIO_TAG);
		for(final String i:res.keySet()){
			Sound.addSoundFromDescriptionFile(i, res.get(i));
		}
	}
	
	/**
	 * Gets the single instance of the AudioManager.
	 * @return Returns the only instance.
	 */
	public static AudioManager getInstance() {
		return MY_INSTANCE;
	}
	
	public void playSound(final Sound sound){
		if(sound.getType().equals(Sound.SoundType.MUSIC)){
			setMusic(sound);
		}else{
			setEffect(sound);
		}
	}

	private void setMusic(final Sound music) {
		if (!GameManager.getInstance().getSettings().isMusicActive()) {
			return;
		}
		myMusic.setMusic(music);
	}
	
	private void setEffect(final Sound effect) {
		if (!GameManager.getInstance().getSettings().isEffectsAudioActive()) {
			return; 
		}

		try {
			Clip tmpClip = AudioSystem.getClip();
			tmpClip.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream(effect.getFileLocation())));

			tmpClip.addLineListener(event -> {
					if (event.getType().equals(LineEvent.Type.STOP)) {
						((Clip)event.getSource()).close();
						//tmpClip.close();
					}
			});

			tmpClip.start();
		} catch (Exception e) {
			ErrorManager.getInstance().notifyError(ErrorManager.ERROR_STARTING_MUSIC, "Something went wrong trying to start a new effect: " + effect);
		}
	}
	
	/**
	 * Stops the actual running music.
	 */
	public void stopCurrent() {
		myMusic.stopCurrent();
	}
	
	private static final class Music {
		
		private AudioInputStream audioInputStream;
		private Clip clip;
		private boolean hasBeenStopped;
		
		private Music() {
			try {
				clip = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				ErrorManager.getInstance().notifyError(ErrorManager.ERROR_INITIALIZING_MUSIC, ERROR_INITIALIZING_MUSIC);
			}
		}
		 
		private void setMusic(final Sound music) {
			this.stopCurrent();
			
			try {	
				audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(music.getFileLocation()));
				clip.open(audioInputStream);
				if (music.mustLoop()) {
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				} else {
					hasBeenStopped = false;
					clip.addLineListener(new LineListener() {
						
						@Override
						public void update(final LineEvent event) {
							if (event.getType().equals(LineEvent.Type.STOP)) {
								hasBeenStopped = false;
								clip.removeLineListener(this);
							}
						}
					});
					clip.start();
				}
			}catch (Exception e) {
				ErrorManager.getInstance().notifyError(ErrorManager.ERROR_STARTING_MUSIC, ERROR_STARTING_MUSIC + " Music: " + toString() + ". Sound: " + music);
			}
		}
		
		private void stopCurrent() {
			try {
				if (audioInputStream != null && clip != null) {
					hasBeenStopped = true;
					clip.stop();
					clip.close();
					audioInputStream.close();
				}
			} catch (Exception e) {
				ErrorManager.getInstance().notifyError(ErrorManager.ERROR_STOPPING_MUSIC, ERROR_STOPPING_MUSIC + " Music: " + toString());
			}
		}
		
		@Override
		public String toString() {
			return "[AudioInputStream: " + audioInputStream + ", Clip: " + clip + ", HasBeenStopped: " + hasBeenStopped + "]";
		}
	}
}
