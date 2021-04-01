package fabbroniko.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.AudioManager.MusicListener;
import fabbroniko.environment.Dimension;
import fabbroniko.environment.Service;
import fabbroniko.error.ResourceNotFoundError;
import fabbroniko.gamestatemanager.AbstractGameState;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.gamestatemanager.IGameStateManager.State;
import fabbroniko.main.Game;
import fabbroniko.resources.Sound;

/**
 * Death Window, it should be shown when the player dies.
 * @author fabbroniko
 */
public final class LostScene extends AbstractGameState implements MusicListener {
	
	private static final LostScene MY_INSTANCE = new LostScene();
	
	private int death;
	private BufferedImage gameOver;
	private int currentDelayCount;
	private boolean musicFinished;
	
	private static final String RES_GAMEOVER_IMAGE = "/fabbroniko/Menu/GameOver.png";
	private static final int TWO_SECONDS = 2000;
	private static final int DELAY_MAX_COUNT = TWO_SECONDS / Game.FPS_MILLIS;
	private static final int GAME_OVER_OFFSET = 50;
	private static final Color BLACK = new Color(0x00000000);
	private static final Color WHITE = new Color(0xffffffff);

	private LostScene() {
		super();
	}

	/**
	 * Gets the single instance of this class.
	 * @return The single instance of this class.
	 */
	public static LostScene getInstance() {
		return MY_INSTANCE;
	}

	@Override
	public void init() {
		try {
			gameOver = ImageIO.read(getClass().getResourceAsStream(RES_GAMEOVER_IMAGE));
		} catch (IOException e) {
			throw new ResourceNotFoundError(RES_GAMEOVER_IMAGE);
		}
		
		musicFinished = false;
		AudioManager.getInstance().setMusicListener(this);
		AudioManager.getInstance().playSound(Sound.getSoundFromName("GameOverSound"));
		currentDelayCount = 0;
	}

	@Override
	public void update() {
		if (SettingsMenuScene.getInstance().musicIsActive() && musicFinished || !SettingsMenuScene.getInstance().musicIsActive() && currentDelayCount > DELAY_MAX_COUNT) {
			GameStateManager.getInstance().setState(State.LEVEL1_STATE);
		}
		currentDelayCount++;
	}
	
	/**
	 * Increments the number of deaths.
	 */
	public void incDeath() {
		death++;
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		g.setColor(BLACK);
		g.fillRect(Service.ORIGIN.getX(), Service.ORIGIN.getY(), gDimension.getWidth(), gDimension.getHeight());
		g.setColor(WHITE);
		g.drawString("X " + death, gDimension.getWidth() / 2, gDimension.getHeight() / 2);
		g.drawImage(gameOver, Service.getXCentredPosition(new Dimension(gameOver.getWidth(), gameOver.getHeight())).getX(), gDimension.getHeight() / 2 - GAME_OVER_OFFSET, null);
	}

	@Override
	public void onStop() {
		musicFinished = true;
	}
}
