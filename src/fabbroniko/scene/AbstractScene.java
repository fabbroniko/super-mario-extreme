package fabbroniko.scene;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Dimension;
import fabbroniko.gamestatemanager.GameManager;
import fabbroniko.main.Drawable;

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

	protected int getCenteredXPositionForString(final String text, final Graphics2D g, final Dimension dimension) {
		return (dimension.getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
	}

	protected int getCenteredXPositionFromSize(final Dimension canvasDimension, final int secondaryWidth) {
		return (canvasDimension.getWidth() - secondaryWidth) / 2;
	}
}
