package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Background;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.resource.ResourceManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public final class MainMenuScene extends AbstractScene implements KeyListener, Scene {

	private static final String TITLE = "Super Mario Extreme Edition";
	private static final String START_GAME_OPTION = "Start";
	private static final String SETTINGS_OPTION = "Settings";
	private static final String QUIT_OPTION = "Quit";
	private static final String HINT = "Arrow UP/DOWN to navigate. ENTER to confirm.";

	private static final int START_OPTION_INDEX = 0;
	private static final int SETTINGS_OPTION_INDEX = 1;
	private static final int QUIT_OPTION_INDEX = 2;

	private static final int STROKE_SIZE = 12;
	private static final int OPTION_ARC_SIZE = 32;
	private static final Vector2D OPTION_RECTANGLE_DIMENSION = new Vector2D(360, 120);
	private static final int OPTION_RECT_TO_TEXT_OFFSET = 92;
	private static final int TITLE_Y = 120;
	private static final int START_OPTION_Y = 260;
	private static final int OPTIONS_OFFSET = 200;
	private static final int HINT_Y = 920;

	private Background bg;

	private int selectedOption;

	private final SceneContextFactory sceneContextFactory;
	private final GameManager gameManager;
	private final AudioManager audioManager;
	private final ResourceManager resourceManager;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;


	public MainMenuScene(final SceneContextFactory sceneContextFactory,
						 final GameManager gameManager,
						 final AudioManager audioManager,
						 final ResourceManager resourceManager) {
		this.sceneContextFactory = sceneContextFactory;
		this.gameManager = gameManager;
		this.audioManager = audioManager;
		this.resourceManager = resourceManager;
	}

	@Override
	public void init() {
		bg = new Background(resourceManager, "menu");
		selectedOption = 0;

		gameManager.addKeyListener(this);
		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.getSceneCanvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.getCanvasDimension();
	}

	@Override
	public void update() {}

	@Override
	public BufferedImage draw() {
		// Draw the background first
		final Vector2D bgPosition = bg.getDrawingPosition();
		graphics.drawImage(bg.getDrawableImage(), bgPosition.getRoundedX(), bgPosition.getRoundedY(), canvasDimension.getWidth(), canvasDimension.getHeight(), null);

		// Activating antialiasing to soften up the look of the strings
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Setting up the parameters to draw the game title
		graphics.setColor(Color.GREEN);
		graphics.setFont(H1_FONT);
		int centeredXPosition = getCenteredXPositionForString(TITLE, graphics, canvasDimension);

		// Draw the game title
		graphics.drawString(TITLE, centeredXPosition, TITLE_Y);

		// Print the menu options
		printMenuOption(START_GAME_OPTION, START_OPTION_Y, graphics, canvasDimension, selectedOption == START_OPTION_INDEX);
		printMenuOption(SETTINGS_OPTION, START_OPTION_Y + OPTIONS_OFFSET, graphics, canvasDimension, selectedOption == SETTINGS_OPTION_INDEX);
		printMenuOption(QUIT_OPTION, START_OPTION_Y + 2*OPTIONS_OFFSET, graphics, canvasDimension, selectedOption == QUIT_OPTION_INDEX);

		// Setting up the configuration for the bottom page hints.
		graphics.setColor(Color.WHITE);
		graphics.setFont(P_S_FONT);
		int x = getCenteredXPositionForString(HINT, graphics, canvasDimension);

		// Draw the hint
		graphics.drawString(HINT, x, HINT_Y);

		// Disabling antialiasing to make sure the next cycle doesn't apply it to the background or whatever is rendered next
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		return canvas;
	}

	@Override
	public void detach() {
		audioManager.stopMusic();
		gameManager.removeKeyListener(this);
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			selectedOption++;
			break;
		case KeyEvent.VK_UP:
			selectedOption--;
			break;
		case KeyEvent.VK_ENTER:
			if (selectedOption == START_OPTION_INDEX) {
				gameManager.openScene(GameScene.class);
			} else if (selectedOption == SETTINGS_OPTION_INDEX) {
				gameManager.openScene(SettingsMenuScene.class);
			} else {
				gameManager.exit();
			}
			break;
		case KeyEvent.VK_ESCAPE:
			gameManager.exit();
			break;
		default:
			break;
		}

		// Allows looping through the options
		if (selectedOption < START_OPTION_INDEX) {
			selectedOption = QUIT_OPTION_INDEX;
		} else if (selectedOption > QUIT_OPTION_INDEX) {
			selectedOption = START_OPTION_INDEX;
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {}

	@Override
	public void keyTyped(final KeyEvent e) {}

	private void printMenuOption(final String text, final int y, final Graphics2D g, final Dimension2D canvasDimension, final boolean isSelected) {
		// Sets up the requirements to draw the rounded rectangle
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(STROKE_SIZE));
		int x = getCenteredXPositionFromSize(canvasDimension, OPTION_RECTANGLE_DIMENSION.getRoundedX());

		// Draws the rectangle
		g.drawRoundRect(x, y, OPTION_RECTANGLE_DIMENSION.getRoundedX(), OPTION_RECTANGLE_DIMENSION.getRoundedY(), OPTION_ARC_SIZE, OPTION_ARC_SIZE);

		// Sets up the requirements to draw the option name within the rectangle
		if(isSelected)
			g.setColor(Color.GREEN);

		g.setFont(P_XXXL_FONT);
		x = getCenteredXPositionForString(text, g, canvasDimension);
		int optionNameY = y + OPTION_RECT_TO_TEXT_OFFSET;

		// Draws the option name within the rectangle
		g.drawString(text, x, optionNameY);
	}
}
