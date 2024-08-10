package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class LostScene extends AbstractScene implements Scene {

	private static final String GAME_OVER_MAIN_TEXT = "Game Over";
	private static final String DEATH_COUNT_TEXT = "Death count: ";
	private static final int SCENE_DURATION_MILLISECONDS = 3000;

	private long initTime;
	private final Vector2D origin = new Vector2D();

	private final SceneContextFactory sceneContextFactory;
	private final GameManager gameManager;
	private final AudioManager audioManager;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public LostScene(final SceneContextFactory sceneContextFactory,
					 final GameManager gameManager,
					 final AudioManager audioManager) {
		this.sceneContextFactory = sceneContextFactory;
		this.gameManager = gameManager;
		this.audioManager = audioManager;
	}

	@Override
	public void init() {
		audioManager.playBackgroundMusic("death", false);
		initTime = System.currentTimeMillis();

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.getSceneCanvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.getCanvasDimension();
	}

	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			gameManager.openScene(GameScene.class);
		}
	}

	@Override
	public BufferedImage draw() {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(origin.getRoundedX(), origin.getRoundedY(), canvasDimension.getWidth(), canvasDimension.getHeight());

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setColor(Color.WHITE);
		graphics.setFont(H1_FONT);
		int centeredX = getCenteredXPositionForString(GAME_OVER_MAIN_TEXT, graphics, canvasDimension);
		int y = (canvasDimension.getHeight() - graphics.getFontMetrics().getHeight()) / 2;

		graphics.drawString(GAME_OVER_MAIN_TEXT, centeredX, y);
		graphics.setFont(P_FONT);
		final String composedDeathCount = DEATH_COUNT_TEXT + gameManager.getDeathCount();
		centeredX = getCenteredXPositionForString(composedDeathCount, graphics, canvasDimension);
		y = (canvasDimension.getHeight() / 2) + (graphics.getFontMetrics().getHeight() / 2);

		graphics.drawString(composedDeathCount, centeredX, y);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		return canvas;
	}

	@Override
	public void detach() {
		audioManager.stopMusic();
	}
}
