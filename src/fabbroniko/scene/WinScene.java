package fabbroniko.scene;

import java.awt.*;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Dimension;
import fabbroniko.environment.Service;
import fabbroniko.gamestatemanager.AbstractStaticScene;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.resources.Sound;

public final class WinScene extends AbstractStaticScene {

	private static final String LEVEL_COMPLETED = "Level Completed";
	private static final int SCENE_DURATION_MILLISECONDS = 6000;

	private long initTime;
	
	@Override
	public void init() {
		AudioManager.getInstance().playSound(Sound.getSoundFromName("WinSound"));
		initTime = System.currentTimeMillis();
	}

	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			AudioManager.getInstance().stopCurrent();
			GameStateManager.getInstance().openScene(new MainMenuScene());
		}
	}

	@Override
	public void drawOnce(final Graphics2D g, final Dimension gDimension) {
		g.setColor(Color.BLACK);
		g.fillRect(Service.ORIGIN.getX(), Service.ORIGIN.getY(), gDimension.getWidth(), gDimension.getHeight());

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 20));
		int centeredX = getCenteredXPositionForString(LEVEL_COMPLETED, g, gDimension);
		int y = (gDimension.getHeight() - g.getFontMetrics().getHeight()) / 2;

		// Draw the Game Over string.
		g.drawString(LEVEL_COMPLETED, centeredX, y);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
