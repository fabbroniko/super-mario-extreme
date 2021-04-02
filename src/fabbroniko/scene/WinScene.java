package fabbroniko.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fabbroniko.Settings;
import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Dimension;
import fabbroniko.environment.Service;
import fabbroniko.environment.AudioManager.MusicListener;
import fabbroniko.error.ResourceNotFoundError;
import fabbroniko.gamestatemanager.AbstractGameState;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.main.GameWindow;
import fabbroniko.resources.Sound;

/**
 * Win Window, it should be shown when the player finishes every level.
 * @author fabbroniko
 */
public final class WinScene extends AbstractGameState implements MusicListener {

	private BufferedImage win;

	private int currentDelayCount;
	private boolean musicFinished;
	
	private static final String RES_WIN_IMAGE = "/fabbroniko/Menu/WinString.png";
	private static final int TWO_SECONDS = 2000;
	private static final int DELAY_MAX_COUNT = TWO_SECONDS / GameWindow.FPS_MILLIS;
	private static final Color BLACK = new Color(0x00000000);
	private static final int WIN_OFFSET = 50;
	
	@Override
	public void init() {
		try {
			win = ImageIO.read(getClass().getResourceAsStream(RES_WIN_IMAGE));
		} catch (IOException e) {
			throw new ResourceNotFoundError(RES_WIN_IMAGE);
		}
		
		musicFinished = false;
		AudioManager.getInstance().setMusicListener(this);
		AudioManager.getInstance().playSound(Sound.getSoundFromName("WinSound"));
		currentDelayCount = 0;
	}

	@Override
	public void update() {
		super.update();
		
		if (Settings.GLOBAL_SETTINGS.isMusicActive() && musicFinished || !Settings.GLOBAL_SETTINGS.isMusicActive() && currentDelayCount > DELAY_MAX_COUNT) {
			GameStateManager.getInstance().openScene(new MainMenuScene());
		}
		currentDelayCount++;
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		g.setColor(BLACK);
		g.fillRect(Service.ORIGIN.getX(), Service.ORIGIN.getY(), gDimension.getWidth(), gDimension.getHeight());
		g.drawImage(win, Service.getXCentredPosition(new Dimension(win.getWidth(), win.getHeight())).getX(), gDimension.getHeight() / 2 - WIN_OFFSET, null);
	}

	@Override
	public void onStop() {
		musicFinished = true;
	}
}
