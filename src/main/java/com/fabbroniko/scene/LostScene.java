package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.input.ActionLessKeyListener;
import com.fabbroniko.main.SceneManager;
import com.fabbroniko.ui.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class LostScene implements Scene, ActionLessKeyListener {

	private static final String GAME_OVER_MAIN_TEXT = "Game Over";
	private static final String DEATH_COUNT_TEXT = "Death count: ";
	private static final int SCENE_DURATION_MILLISECONDS = 3000;

	private long initTime;
	private final Vector2D origin = new Vector2D();

	private final SceneContextFactory sceneContextFactory;
	private final AudioManager audioManager;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final int deathCount;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public LostScene(final SceneContextFactory sceneContextFactory,
					 final AudioManager audioManager,
					 final SceneManager sceneManager,
					 final TextFactory textFactory,
					 final int deathCount) {

		this.sceneContextFactory = sceneContextFactory;
		this.audioManager = audioManager;
		this.sceneManager = sceneManager;
		this.textFactory = textFactory;
		this.deathCount = deathCount;
	}

	@Override
	public void init() {
		audioManager.playBackgroundMusic("death", false);
		initTime = System.currentTimeMillis();

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.canvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.canvasDimension();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void update() {
		if((System.currentTimeMillis() - initTime) > SCENE_DURATION_MILLISECONDS) {
			sceneManager.openGameScene();
		}
	}

	@Override
	public BufferedImage draw() {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(origin.getRoundedX(), origin.getRoundedY(), canvasDimension.width(), canvasDimension.height());

		final BufferedImage gameOver = textFactory.createParagraph(GAME_OVER_MAIN_TEXT, Color.WHITE);
		int x = (canvasDimension.width() - gameOver.getWidth()) / 2;
		graphics.drawImage(gameOver, null, x, 200);

		final BufferedImage deathCountImage = textFactory.createSmallParagraph(DEATH_COUNT_TEXT + deathCount, Color.WHITE);
		x = (canvasDimension.width() - deathCountImage.getWidth()) / 2;
		graphics.drawImage(deathCountImage, null, x, 300);

		return canvas;
	}
}
