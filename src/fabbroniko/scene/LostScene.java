package fabbroniko.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Dimension;
import fabbroniko.environment.Service;
import fabbroniko.error.ResourceNotFoundError;
import fabbroniko.gamestatemanager.AbstractGameState;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.resources.Sound;

/**
 * Death Window, it should be shown when the player dies.
 * @author fabbroniko
 */
public final class LostScene extends AbstractGameState {

	private final int deathCount;
	private BufferedImage gameOver;
	private long initTime;
	
	private static final String RES_GAMEOVER_IMAGE = "/fabbroniko/Menu/GameOver.png";
	private static final int SCENE_DURATION_MILLISECONDS = 3000;
	private static final int GAME_OVER_OFFSET = 50;
	private static final Color BLACK = new Color(0x00000000);
	private static final Color WHITE = new Color(0xffffffff);

	public LostScene(final int deathCount) {
		super();

		this.deathCount = deathCount;
	}

	@Override
	public void init() {
		try {
			gameOver = ImageIO.read(getClass().getResourceAsStream(RES_GAMEOVER_IMAGE));
		} catch (IOException e) {
			throw new ResourceNotFoundError(RES_GAMEOVER_IMAGE);
		}

		AudioManager.getInstance().playSound(Sound.getSoundFromName("GameOverSound"));
		initTime = System.currentTimeMillis();
	}

	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			AudioManager.getInstance().stopCurrent();
			GameStateManager.getInstance().openScene(new GameScene());
		}
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		g.setColor(BLACK);
		g.fillRect(Service.ORIGIN.getX(), Service.ORIGIN.getY(), gDimension.getWidth(), gDimension.getHeight());
		g.setColor(WHITE);
		g.drawString("X " + deathCount, gDimension.getWidth() / 2, gDimension.getHeight() / 2);
		g.drawImage(gameOver, Service.getXCentredPosition(new Dimension(gameOver.getWidth(), gameOver.getHeight())).getX(), gDimension.getHeight() / 2 - GAME_OVER_OFFSET, null);
	}
}
