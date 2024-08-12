package com.fabbroniko.scene;

import com.fabbroniko.environment.Background;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.SceneManager;
import com.fabbroniko.resource.ResourceManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_UP;

public final class MainMenuScene implements Scene {

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

	private Background bg;

	private int selectedOption;

	private final SceneContextFactory sceneContextFactory;
	private final ResourceManager resourceManager;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final OptionFactory optionFactory;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public MainMenuScene(final SceneContextFactory sceneContextFactory,
                         final ResourceManager resourceManager,
                         final SceneManager sceneManager,
                         final TextFactory textFactory,
						 final OptionFactory optionFactory) {

		this.sceneContextFactory = sceneContextFactory;
		this.resourceManager = resourceManager;
		this.sceneManager = sceneManager;
		this.textFactory = textFactory;
        this.optionFactory = optionFactory;
    }

	@Override
	public void init() {
		bg = new Background(resourceManager, "menu");
		selectedOption = 0;

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.getSceneCanvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.getCanvasDimension();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public BufferedImage draw() {
		final Vector2D bgPosition = bg.getDrawingPosition();
		graphics.drawImage(bg.getDrawableImage(), bgPosition.getRoundedX(), bgPosition.getRoundedY(), canvasDimension.width(), canvasDimension.height(), null);

		final BufferedImage title = textFactory.createHeader(TITLE, Color.GREEN);
		int centeredXPosition = getCenteredXPosition(title, canvasDimension);
		graphics.drawImage(title, null, centeredXPosition, TITLE_Y);

		printMenuOption(START_GAME_OPTION, START_OPTION_Y, selectedOption == START_OPTION_INDEX);
		printMenuOption(SETTINGS_OPTION, SETTINGS_OPTION_Y, selectedOption == SETTINGS_OPTION_INDEX);
		printMenuOption(QUIT_OPTION, QUIT_OPTION_Y, selectedOption == QUIT_OPTION_INDEX);

		final BufferedImage hint = textFactory.createSmallParagraph(HINT, Color.WHITE);
		centeredXPosition = getCenteredXPosition(hint, canvasDimension);
		graphics.drawImage(hint, null, centeredXPosition, HINT_Y);

		return canvas;
	}

	private void printMenuOption(final String text, final int y, final boolean isSelected) {
		final Color color = isSelected ? Color.GREEN : Color.WHITE;

		final BufferedImage startGameOption = optionFactory.getMainMenuOption(text, color);
		int x = getCenteredXPosition(startGameOption, canvasDimension);
		graphics.drawImage(startGameOption, null, x, y);
	}

	@Override
	public void keyPressed(final KeyEvent event) {
	}

	@Override
	public void keyTyped(final KeyEvent event) {
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
