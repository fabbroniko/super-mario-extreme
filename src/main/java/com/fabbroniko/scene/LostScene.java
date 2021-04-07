package com.fabbroniko.scene;

import java.awt.*;

import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.Service;

/**
 * The LostScene is a very simple scene, it just shows Game Over and the number of death on a simple background.
 * It also plays a music track. The user is redirected back to the level after the music track is over.
 *
 * Given the static nature of this scene it uses the drawOnce method that allows the program to draw the canvas only once
 * instead of drawing it for each frame.
 */
public final class LostScene extends AbstractStaticScene {

	private static final String GAME_OVER_MAIN_TEXT = "Game Over";
	private static final String DEATH_COUNT_TEXT = "Death count: ";
	private static final int SCENE_DURATION_MILLISECONDS = 3000;

	private final int deathCount;
	private long initTime;

	public LostScene(final int deathCount) {
		super();

		this.deathCount = deathCount;
	}

	@Override
	public void init() {
		audioManager.playBackgroundMusic("death", false);
		initTime = System.currentTimeMillis();
	}

	/**
	 * On each frame check if enough time elapsed before opening the game scene.
	 */
	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			gameManager.openScene(new GameScene());
		}
	}

	/**
	 * Draw the Lost Scene in the Game Frame.
	 * Due to the static nature of this scene it can be drawn only once rather than drawing it for each frame.
	 * @param g The canvas we use to paint the scene.
	 * @param gDimension The dimensions of the canvas.
	 */
	@Override
	protected void drawOnce(final Graphics2D g, final Dimension gDimension) {
		// Filling the whole canvas with Black
		g.setColor(Color.BLACK);
		g.fillRect(Service.ORIGIN.getX(), Service.ORIGIN.getY(), gDimension.getWidth(), gDimension.getHeight());

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		/*
		 * Sets up the parameters for the Game Over string displayed in this scene.
		 * Sets the color of the string to White.
		 * Sets the Font
		 * Calculates the coordinate of the origin of the string in order to center it in the X axis
		 */
		g.setColor(Color.WHITE);
		g.setFont(H1_FONT);
		int centeredX = getCenteredXPositionForString(GAME_OVER_MAIN_TEXT, g, gDimension);
		int y = (gDimension.getHeight() - g.getFontMetrics().getHeight()) / 2;

		// Draw the Game Over string.
		g.drawString(GAME_OVER_MAIN_TEXT, centeredX, y);

		/*
		 * Sets up the parameters for the death count string.
		 * Sets the font
		 * Calculates the coordinate of the origin of the string in order for it to be centered in the X axis and below the Game Over string by a certain padding
		 */
		g.setFont(P_FONT);
		final String composedDeathCount = DEATH_COUNT_TEXT + deathCount;
		centeredX = getCenteredXPositionForString(composedDeathCount, g, gDimension);
		y = (gDimension.getHeight() / 2) + (g.getFontMetrics().getHeight() / 2);

		// Draw the death count string
		g.drawString(composedDeathCount, centeredX, y);

		// Prevents this scene to be rendered again for subsequent frames and resets antialiasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
