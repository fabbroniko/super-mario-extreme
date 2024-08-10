package com.fabbroniko.scene;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.image.BufferedImage;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.resource.ResourceManager;

public final class WinScene extends AbstractScene implements Scene {

	private static final String LEVEL_COMPLETED = "Level Completed";
	private static final int SCENE_DURATION_MILLISECONDS = 6000;

	private long initTime;
	private final Vector2D origin = new Vector2D();

	private final SceneContextFactory sceneContextFactory;
	private final GameManager gameManager;
	private final AudioManager audioManager;
	private final ResourceManager resourceManager;

	public WinScene(final GameManager gameManager, SceneContextFactory sceneContextFactory, AudioManager audioManager, ResourceManager resourceManager) {
		super(gameManager, audioManager, resourceManager);
        this.sceneContextFactory = sceneContextFactory;
		this.gameManager = gameManager;
        this.audioManager = audioManager;
        this.resourceManager = resourceManager;
    }

	@Override
	public void init() {
		audioManager.playBackgroundMusic("victory", false);
		initTime = System.currentTimeMillis();
	}

	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			gameManager.openScene(MainMenuScene.class);
		}
	}

	@Override
	public BufferedImage draw() {
		final SceneContext sceneContext = sceneContextFactory.create();
		final BufferedImage canvas = sceneContext.getSceneCanvas();
		final Graphics2D graphics = (Graphics2D) canvas.getGraphics();
		final Dimension2D canvasDimension = sceneContext.getCanvasDimension();

		// Fill in the background
		graphics.setColor(Color.BLACK);
		graphics.fillRect(origin.getRoundedX(), origin.getRoundedY(), canvasDimension.getWidth(), canvasDimension.getHeight());

		// Activating the antialiasing to smooth out the strings
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Setting up the parameters to draw the strings
		graphics.setColor(Color.WHITE);
		graphics.setFont(H1_FONT);
		int centeredX = getCenteredXPositionForString(LEVEL_COMPLETED, graphics, canvasDimension);
		int y = (canvasDimension.getHeight() - graphics.getFontMetrics().getHeight()) / 2;

		// Draw the Game Over string.
		graphics.drawString(LEVEL_COMPLETED, centeredX, y);

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		return canvas;
	}

	@Override
	public void draw(final Graphics2D g, final Vector2D canvasDimension) {
		g.drawImage(draw(), null, 0, 0);
	}
}
