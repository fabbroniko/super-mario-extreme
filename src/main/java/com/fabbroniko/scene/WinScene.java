package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public final class WinScene extends AbstractScene implements Scene {

	private static final String LEVEL_COMPLETED = "Level Completed";
	private static final int SCENE_DURATION_MILLISECONDS = 6000;

	private final SceneContextFactory sceneContextFactory;
	private final AudioManager audioManager;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;
	private long initTime;
	private boolean isClosed = false;

	public WinScene(final SceneContextFactory sceneContextFactory,
					final AudioManager audioManager) {
        this.sceneContextFactory = sceneContextFactory;
        this.audioManager = audioManager;
    }

	@Override
	public void init() {
		audioManager.playBackgroundMusic("victory", false);
		initTime = System.currentTimeMillis();

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.getSceneCanvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.getCanvasDimension();
	}

	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			isClosed = true;
		}
	}

	@Override
	public BufferedImage draw() {
		// Fill in the background
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, canvasDimension.width(), canvasDimension.height());

		// Activating the antialiasing to smooth out the strings
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Setting up the parameters to draw the strings
		graphics.setColor(Color.WHITE);
		graphics.setFont(H1_FONT);
		int centeredX = getCenteredXPositionForString(LEVEL_COMPLETED, graphics, canvasDimension);
		int y = (canvasDimension.height() - graphics.getFontMetrics().getHeight()) / 2;

		// Draw the Game Over string.
		graphics.drawString(LEVEL_COMPLETED, centeredX, y);

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		return canvas;
	}

	@Override
	public void detach() {
		audioManager.stopMusic();
	}

	@Override
	public boolean isClosed() {
		return isClosed;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
