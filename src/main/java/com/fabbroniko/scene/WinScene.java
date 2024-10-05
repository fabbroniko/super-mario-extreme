package com.fabbroniko.scene;

import com.fabbroniko.audio.MusicPlayerProvider;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.InitializableDrawable;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

@Component
public final class WinScene implements Scene {

	private static final String LEVEL_COMPLETED = "Level Completed";
	private static final int SCENE_DURATION_MILLISECONDS = 6000;

	private final SceneContextFactory sceneContextFactory;
	private final MusicPlayerProvider musicPlayerProvider;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final BackgroundLoader backgroundLoader;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;
	private long initTime;
	private DrawableResource background;

	public WinScene(final SceneContextFactory sceneContextFactory,
                    final MusicPlayerProvider musicPlayerProvider,
                    final SceneManager sceneManager,
					final TextFactory textFactory,
					final BackgroundLoader backgroundLoader) {

        this.sceneContextFactory = sceneContextFactory;
        this.musicPlayerProvider = musicPlayerProvider;
		this.sceneManager = sceneManager;
        this.textFactory = textFactory;
		this.backgroundLoader = backgroundLoader;
    }

	@Override
	public void init() {
		musicPlayerProvider.getMusicPlayer().play("victory", false);
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
			sceneManager.openScene(MainMenuScene.class);
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

	@Override
	public void close() {
		musicPlayerProvider.getMusicPlayer().stop();
	}
}
