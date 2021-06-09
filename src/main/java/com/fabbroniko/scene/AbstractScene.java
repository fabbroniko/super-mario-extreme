package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.resource.ResourceManager;

import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics2D;

public abstract class AbstractScene {

	protected static final Font H1_FONT = new JPanel().getFont().deriveFont(Font.BOLD, 80);
	protected static final Font P_XXXL_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 80);
	protected static final Font P_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 48);
	protected static final Font P_S_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 40);

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

	public abstract void update();

	public abstract void draw(final Graphics2D g, final Vector2D canvasDimension);

	protected int getCenteredXPositionForString(final String text, final Graphics2D g, final Vector2D dimension) {
		return (dimension.getRoundedX() - g.getFontMetrics().stringWidth(text)) / 2;
	}

	protected int getCenteredXPositionFromSize(final Vector2D canvasDimension, final int secondaryWidth) {
		return (canvasDimension.getRoundedX() - secondaryWidth) / 2;
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
