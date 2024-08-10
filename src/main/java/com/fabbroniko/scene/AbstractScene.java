package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension2D;
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

	protected AbstractScene(final GameManager gameManager, final AudioManager audioManager, final ResourceManager resourceManager) {
		this.gameManager = gameManager;
		this.audioManager = audioManager;
		this.resourceManager = resourceManager;
	}

	public void detachScene() {
		audioManager.stopMusic();
	}

	public abstract void init();

	public abstract void update();

	public abstract void draw(final Graphics2D g, final Vector2D canvasDimension);

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
}
