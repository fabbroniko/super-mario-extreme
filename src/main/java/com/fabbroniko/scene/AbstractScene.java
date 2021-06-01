package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.GameManager;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.Time;
import com.fabbroniko.resource.ResourceManager;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public abstract class AbstractScene implements Drawable {

	protected static final Font H1_FONT = new JPanel().getFont().deriveFont(Font.BOLD, 20);
	protected static final Font P_XXXL_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 20);
	protected static final Font P_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 12);
	protected static final Font P_S_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 10);

	protected GameManager gameManager;
	protected AudioManager audioManager;
	protected ResourceManager resourceManager;

	private int fpsCounts = 0, oldFpsCount = 0;
	private double cumulativeDeltas = 0;

	protected AbstractScene(final GameManager gameManager) {
		this.gameManager = gameManager;
		this.audioManager = gameManager.getAudioManager();
		this.resourceManager = gameManager.getResourceManager();
	}

	public void detachScene() {
		audioManager.stopCurrent();
	}

	public abstract void init();
	
	@Override
	public void update() {}
	
	@Override
	public void draw(final Graphics2D g, final Dimension dimension) {
		drawScene(g, dimension);
		postDraw(g, dimension);
	}

	protected abstract void drawScene(final Graphics2D g, final Dimension dimension);

	private void postDraw(final Graphics2D g, final Dimension dimension) {
		// Showing fps in all screens
		if(gameManager.getSettings().isShowFps()) {
			cumulativeDeltas += Time.deltaTime();
			fpsCounts++;

			int drawingFps = oldFpsCount;
			if (cumulativeDeltas > 1) {
				drawingFps = fpsCounts;
				oldFpsCount = fpsCounts;
				cumulativeDeltas = 0;
				fpsCounts = 0;
			}

			if(drawingFps < 30)
				g.setColor(Color.RED);
			else
				g.setColor(Color.GREEN);

			g.setFont(g.getFont().deriveFont(14.0f));

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawString(String.valueOf(drawingFps), 292, 15);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	protected int getCenteredXPositionForString(final String text, final Graphics2D g, final Dimension dimension) {
		return (dimension.getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
	}

	protected int getCenteredXPositionFromSize(final Dimension canvasDimension, final int secondaryWidth) {
		return (canvasDimension.getWidth() - secondaryWidth) / 2;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
}
