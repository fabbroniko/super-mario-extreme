package fabbroniko.scene;

import java.awt.*;

import fabbroniko.environment.Dimension;
import fabbroniko.environment.Service;
import fabbroniko.resources.Sound;

/**
 * The WinScene is a very simple scene, it shows a "Level Completed" string on a simple background and it plays
 * the Victory music track. The user is redirected back to the main menu after x seconds (enough for the music
 * track to complete).
 *
 * Given the static nature of this scene it uses the drawOnce method that allows the program to draw the canvas only once
 * instead of drawing it for each frame.
 */
public final class WinScene extends AbstractStaticScene {

	private static final String LEVEL_COMPLETED = "Level Completed";
	private static final int SCENE_DURATION_MILLISECONDS = 6000;

	private long initTime;

	@Override
	public void init() {
		audioManager.playSound(Sound.getSoundFromName("WinSound"));
		initTime = System.currentTimeMillis();
	}

	/**
	 * On each frame the update method checks if enough time has elapsed before opening the Main Menu scene.
	 */
	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			gameManager.openScene(new MainMenuScene());
		}
	}

	/**
	 * Draw the WinScene.
	 * This is a simple static scene showing just a string. Nothing special here.
	 * @param g The canvas to paint on.
	 * @param gDimension The dimension of the canvas
	 */
	@Override
	public void drawOnce(final Graphics2D g, final Dimension gDimension) {
		// Fill in the background
		g.setColor(Color.BLACK);
		g.fillRect(Service.ORIGIN.getX(), Service.ORIGIN.getY(), gDimension.getWidth(), gDimension.getHeight());

		// Activating the antialiasing to smooth out the strings
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Setting up the parameters to draw the strings
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 20));
		int centeredX = getCenteredXPositionForString(LEVEL_COMPLETED, g, gDimension);
		int y = (gDimension.getHeight() - g.getFontMetrics().getHeight()) / 2;

		// Draw the Game Over string.
		g.drawString(LEVEL_COMPLETED, centeredX, y);

		// Disabling the antialiasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}