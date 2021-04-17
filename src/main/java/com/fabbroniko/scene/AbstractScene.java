package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.gamestatemanager.GameManager;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.resource.ResourceManager;

import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics2D;

public abstract class AbstractScene implements Drawable {

	protected static final Font H1_FONT = new JPanel().getFont().deriveFont(Font.BOLD, 20);
	protected static final Font P_XXXL_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 20);
	protected static final Font P_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 12);
	protected static final Font P_S_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 10);

	protected GameManager gameManager;
	protected AudioManager audioManager;
	protected ResourceManager resourceManager;

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
	public abstract void draw(final Graphics2D g, final Dimension gDimension);

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
