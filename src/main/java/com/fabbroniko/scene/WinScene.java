package com.fabbroniko.scene;

import com.fabbroniko.audio.AudioManager;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.ActionLessKeyListener;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.InitializableDrawable;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class WinScene implements Scene, ActionLessKeyListener {

	private static final String LEVEL_COMPLETED = "Level Completed";
	private static final int SCENE_DURATION_MILLISECONDS = 6000;

	private final SceneContextFactory sceneContextFactory;
	private final AudioManager audioManager;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final BackgroundLoader backgroundLoader;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;
	private long initTime;
	private DrawableResource background;

	public WinScene(final SceneContextFactory sceneContextFactory,
                    final AudioManager audioManager,
                    final SceneManager sceneManager,
					final TextFactory textFactory,
					final BackgroundLoader backgroundLoader) {

        this.sceneContextFactory = sceneContextFactory;
        this.audioManager = audioManager;
		this.sceneManager = sceneManager;
        this.textFactory = textFactory;
		this.backgroundLoader = backgroundLoader;
    }

	@Override
	public void init() {
		audioManager.playBackgroundMusic("victory", false);
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
			sceneManager.openMainMenu();
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

		final BufferedImage levelCompleted = textFactory.createParagraph(LEVEL_COMPLETED, Color.WHITE);
		int x = (canvasDimension.width() - levelCompleted.getWidth()) / 2;
		graphics.drawImage(levelCompleted, null, x, 200);

		return canvas;
	}
}
