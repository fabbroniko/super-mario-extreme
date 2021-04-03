package fabbroniko.scene;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Dimension;
import fabbroniko.gamestatemanager.GameManager;
import fabbroniko.main.Drawable;
import fabbroniko.main.KeyDependent;

public abstract class AbstractScene implements Drawable, KeyDependent {

	protected GameManager gameManager;
	protected AudioManager audioManager;

	public void attachGameManager(final GameManager gameManager) {
		this.gameManager = gameManager;
		this.audioManager = gameManager.getAudioManager();
	}

	public void detachScene() {
		audioManager.stopCurrent();
	}

	public abstract void init();
	
	@Override
	public void update() {}
	
	@Override
	public abstract void draw(final Graphics2D g, final Dimension gDimension);
	
	@Override
	public void keyPressed(final KeyEvent e) {}
	
	@Override
	public void keyReleased(final KeyEvent e) {}

	protected int getCenteredXPositionForString(final String text, final Graphics2D g, final Dimension dimension) {
		return (dimension.getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
	}

	protected int getCenteredXPositionFromSize(final Dimension canvasDimension, final int secondaryWidth) {
		return (canvasDimension.getWidth() - secondaryWidth) / 2;
	}
}
