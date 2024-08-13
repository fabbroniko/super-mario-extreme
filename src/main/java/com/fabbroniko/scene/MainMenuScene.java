package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.UIKeyListener;
import com.fabbroniko.main.BackgroundLoader;
import com.fabbroniko.main.DrawableResource;
import com.fabbroniko.main.SceneManager;
import com.fabbroniko.ui.OptionFactory;
import com.fabbroniko.ui.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_UP;

public final class MainMenuScene implements Scene, UIKeyListener {

	private static final String TITLE = "Super Mario Extreme Edition";
	private static final String START_GAME_OPTION = "Start";
	private static final String SETTINGS_OPTION = "Settings";
	private static final String QUIT_OPTION = "Quit";
	private static final String HINT = "Arrow UP/DOWN to navigate. ENTER to confirm.";

	private static final int START_OPTION_INDEX = 0;
	private static final int SETTINGS_OPTION_INDEX = 1;
	private static final int QUIT_OPTION_INDEX = 2;

	private static final int TITLE_Y = 120;
	private static final int START_OPTION_Y = 260;
	private static final int SETTINGS_OPTION_Y = 460;
	private static final int QUIT_OPTION_Y = 660;
	private static final int HINT_Y = 920;

	private DrawableResource background;

	private int selectedOption;

	private final SceneContextFactory sceneContextFactory;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final OptionFactory optionFactory;
	private final BackgroundLoader backgroundLoader;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public MainMenuScene(final SceneContextFactory sceneContextFactory,
                         final SceneManager sceneManager,
                         final TextFactory textFactory,
						 final OptionFactory optionFactory,
						 final BackgroundLoader backgroundLoader) {

		this.sceneContextFactory = sceneContextFactory;
		this.sceneManager = sceneManager;
		this.textFactory = textFactory;
        this.optionFactory = optionFactory;
		this.backgroundLoader = backgroundLoader;
    }

	@Override
	public void init() {
		background = backgroundLoader.createStaticBackground("menu").getDrawableResource();
		selectedOption = 0;

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.canvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.canvasDimension();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void update() {
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

		final BufferedImage title = textFactory.createHeader(TITLE, Color.GREEN);
		int x = (canvasDimension.width() - title.getWidth()) / 2;
		graphics.drawImage(title, null, x, TITLE_Y);

		printMenuOption(START_GAME_OPTION, START_OPTION_Y, selectedOption == START_OPTION_INDEX);
		printMenuOption(SETTINGS_OPTION, SETTINGS_OPTION_Y, selectedOption == SETTINGS_OPTION_INDEX);
		printMenuOption(QUIT_OPTION, QUIT_OPTION_Y, selectedOption == QUIT_OPTION_INDEX);

		final BufferedImage hint = textFactory.createSmallParagraph(HINT, Color.WHITE);
		x = (canvasDimension.width() - hint.getWidth()) / 2;
		graphics.drawImage(hint, null, x, HINT_Y);

		return canvas;
	}

	private void printMenuOption(final String text, final int y, final boolean isSelected) {
		final Color color = isSelected ? Color.GREEN : Color.WHITE;

		final BufferedImage startGameOption = optionFactory.getMainMenuOption(text, color);
		int x = (canvasDimension.width() - startGameOption.getWidth()) / 2;
		graphics.drawImage(startGameOption, null, x, y);
	}

	@Override
	public void keyReleased(final KeyEvent event) {
		switch(event.getKeyCode()) {
			case VK_DOWN:
				selectedOption++;
				break;
			case VK_UP:
				selectedOption--;
				break;
			case VK_ENTER:
				if (selectedOption == START_OPTION_INDEX) {
					sceneManager.openGameScene();
				} else if (selectedOption == SETTINGS_OPTION_INDEX) {
					sceneManager.openSettings();
				} else {
					sceneManager.quit();
				}
				break;
			case VK_ESCAPE:
				sceneManager.quit();
				break;
			default:
				break;
		}

		if (selectedOption < START_OPTION_INDEX) {
			selectedOption = QUIT_OPTION_INDEX;
		} else if (selectedOption > QUIT_OPTION_INDEX) {
			selectedOption = START_OPTION_INDEX;
		}
	}
}
