package com.fabbroniko.scene;

import com.fabbroniko.audio.MusicPlayer;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.ActionLessKeyListener;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.InitializableDrawable;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class LostScene implements Scene, ActionLessKeyListener {

	private static final String GAME_OVER_MAIN_TEXT = "Game Over";
	private static final String DEATH_COUNT_TEXT = "Death count: ";
	private static final int SCENE_DURATION_MILLISECONDS = 3000;

	private long initTime;

	private final SceneContextFactory sceneContextFactory;
	private final MusicPlayer musicPlayer;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final int deathCount;
	private final BackgroundLoader backgroundLoader;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;
	private DrawableResource background;

	public LostScene(final SceneContextFactory sceneContextFactory,
					 final MusicPlayer musicPlayer,
					 final SceneManager sceneManager,
					 final TextFactory textFactory,
					 final int deathCount,
					 final BackgroundLoader backgroundLoader) {

		this.sceneContextFactory = sceneContextFactory;
		this.musicPlayer = musicPlayer;
		this.sceneManager = sceneManager;
		this.textFactory = textFactory;
		this.deathCount = deathCount;
		this.backgroundLoader = backgroundLoader;
	}

	@Override
	public void init() {
		musicPlayer.play("death", false);
		initTime = System.currentTimeMillis();

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.canvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.canvasDimension();

		final InitializableDrawable initializableBackground = backgroundLoader.createSimpleColorBackground(Color.BLACK, canvasDimension);
		initializableBackground.init();
		this.background = initializableBackground.getDrawableResource();

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
		graphics.drawImage(
				background.image(),
				background.position().getRoundedX(),
				background.position().getRoundedY(),
				canvasDimension.width(),
				canvasDimension.height(),
				null
		);

		final BufferedImage gameOver = textFactory.createParagraph(GAME_OVER_MAIN_TEXT, Color.WHITE);
		int x = (canvasDimension.width() - gameOver.getWidth()) / 2;
		graphics.drawImage(gameOver, null, x, 200);

		final BufferedImage deathCountImage = textFactory.createSmallParagraph(DEATH_COUNT_TEXT + deathCount, Color.WHITE);
		x = (canvasDimension.width() - deathCountImage.getWidth()) / 2;
		graphics.drawImage(deathCountImage, null, x, 300);

		return canvas;
	}

	@Override
	public void close() {
		musicPlayer.stop();
	}
}
