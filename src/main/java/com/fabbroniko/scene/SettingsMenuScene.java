package com.fabbroniko.scene;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.UIKeyListener;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.environment.SettingsProvider;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public final class SettingsMenuScene implements Scene, UIKeyListener {

	// Constant strings
	private static final String HINT_1 = "Arrow UP/DOWN to navigate. ENTER to modify.";
	private static final String HINT_2 = "Press a Key to select a new binding, then ENTER to confirm.";

	// Absolute coordinates
	private static final int FIRST_OPTION_Y = 0;
	private static final int OPTIONS_Y_OFFSET = 80;
	private static final int HINT_Y = 920;
	private static final int OPTION_NAME_X = 40;
	private static final int OPTION_VALUE_X = 1000;

	// Options Index
	private static final int SELECTION_LEFT_KEY = 0;
	private static final int SELECTION_RIGHT_KEY = 1;
	private static final int SELECTION_JUMP_KEY = 2;
	private static final int SELECTION_MUSIC = 3;
	private static final int SELECTION_EFFECTS = 4;
	private static final int SELECTION_SHOW_FPS = 5;
	private static final int SELECTION_FPS_CAP = 6;

	private static final int MAX_SELECTION = 6;

	// Fields
	private DrawableResource background;
	private int currentlyDrawingOption;
	private int currentSelection;
	private boolean keyListening;

	private final SceneContextFactory sceneContextFactory;
	private final SettingsProvider settingsProvider;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final BackgroundLoader backgroundLoader;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public SettingsMenuScene(final SceneContextFactory sceneContextFactory,
                             final SettingsProvider settingsProvider,
                             final SceneManager sceneManager,
                             final TextFactory textFactory,
							 final BackgroundLoader backgroundLoader) {

		this.sceneContextFactory = sceneContextFactory;
		this.settingsProvider = settingsProvider;
		this.sceneManager = sceneManager;
        this.textFactory = textFactory;
        this.backgroundLoader = backgroundLoader;
    }

	@Override
	public void init() {
		background = backgroundLoader.createStaticBackground("menu").getDrawableResource();

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.canvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.canvasDimension();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void update() {}

	@Override
	public BufferedImage draw() {
		graphics.drawImage(background.image(),
				background.position().getRoundedX(),
				background.position().getRoundedY(),
				canvasDimension.width(),
				canvasDimension.height(),
				null
		);

		currentlyDrawingOption = 1;
		printOption("Left Key: ", settingsProvider.getSettings().getLeftMovementKeyCode());
		printOption("Right Key: ", settingsProvider.getSettings().getRightMovementKeyCode());
		printOption("Jump Key: ", settingsProvider.getSettings().getJumpKeyCode());
		printOption("Music: ", settingsProvider.getSettings().isMusicActive());
		printOption("Effects: ", settingsProvider.getSettings().isEffectsAudioActive());
		printOption("Show FPS: ", settingsProvider.getSettings().isShowFps());
		printOption("FPS Cap: ", String.valueOf(settingsProvider.getSettings().getFpsCap()));

		final String hint = keyListening ? HINT_2 : HINT_1;
		final BufferedImage hintImage = textFactory.createSmallParagraph(hint, Color.WHITE);
		int x = (canvasDimension.width() - hintImage.getWidth()) / 2;
		graphics.drawImage(hintImage, null, x, HINT_Y);

		return canvas;
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				sceneManager.openMainMenu();
				break;
			case KeyEvent.VK_UP:
				specialKeyUpHandler();
				break;
			case KeyEvent.VK_DOWN:
				specialKeyDownHandler();
				break;
			case KeyEvent.VK_ENTER:
				specialKeyEnterHandler();
				break;
			default:
				keyHandler(e.getKeyCode());
				break;
		}
	}

	private void printOption(final String optionName,
							 final int keyCode) {

		printOption(optionName, getKeyString(keyCode));
	}

	private void printOption(final String optionName,
							 final boolean optionValue) {

		final String sOptionValue = optionValue ? "ON" : "OFF";

		printOption(optionName, sOptionValue);
	}

	private void printOption(final String optionName,
							 final String optionValue) {

		Color textColor = Color.WHITE;
		if((currentSelection + 1) == currentlyDrawingOption)
			textColor = Color.GREEN;

		final BufferedImage optionNameImage = textFactory.createParagraph(optionName, textColor);
		final BufferedImage optionValueImage = textFactory.createParagraph(optionValue, textColor);

		int y = FIRST_OPTION_Y + (OPTIONS_Y_OFFSET * currentlyDrawingOption);
		graphics.drawImage(optionNameImage, null, OPTION_NAME_X, y);
		graphics.drawImage(optionValueImage, null, OPTION_VALUE_X, y);

		currentlyDrawingOption++;
	}

	private String getKeyString(final int keyCode) {
		String tmp;

		if (keyCode == KeyEvent.VK_UP) {
			tmp = "UP";
		} else if (keyCode == KeyEvent.VK_LEFT) {
			tmp = "LEFT";
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			tmp = "RIGHT";
		} else if (keyCode == KeyEvent.VK_DOWN) {
			tmp = "DOWN";
		} else if (keyCode == KeyEvent.VK_SPACE) {
			tmp = "SPACE";
		} else {
			tmp = "" + (char) keyCode;
		}

		return tmp;
	}

	private void specialKeyUpHandler() {
		if(!keyListening) {
			currentSelection--;
			currentSelection = currentSelection < 0 ? MAX_SELECTION : currentSelection;
			return;
		}

		keyHandler(KeyEvent.VK_UP);
	}

	private void specialKeyDownHandler() {
		if (!keyListening) {
			currentSelection++;
			currentSelection = currentSelection > MAX_SELECTION ? 0 : currentSelection;
			return;
		}

		keyHandler(KeyEvent.VK_DOWN);
	}

	private void specialKeyEnterHandler() {
		if(currentSelection == SELECTION_EFFECTS)
			settingsProvider.getSettings().invertEffectActive();
		else if (currentSelection == SELECTION_MUSIC)
			settingsProvider.getSettings().invertMusicActive();
		else if (currentSelection == SELECTION_SHOW_FPS)
			settingsProvider.getSettings().invertShowFps();
		else if (currentSelection == SELECTION_FPS_CAP)
			settingsProvider.getSettings().setFpsCap(nextFps(settingsProvider.getSettings().getFpsCap()));
		else
			keyListening ^= true;

		settingsProvider.saveSettings();
	}

	private int nextFps(final int fps) {
        return switch (fps) {
            case 30 -> 60;
            case 60 -> 90;
            case 90 -> 120;
            default -> 30;
        };
	}

	private void keyHandler(final int keyCode) {
		if(!keyListening)
			return;

		if(currentSelection == SELECTION_LEFT_KEY) {
			settingsProvider.getSettings().setLeftMovementKeyCode(keyCode);
		} else if (currentSelection == SELECTION_RIGHT_KEY) {
			settingsProvider.getSettings().setRightMovementKeyCode(keyCode);
		} else if (currentSelection == SELECTION_JUMP_KEY) {
			settingsProvider.getSettings().setJumpKeyCode(keyCode);
		}

		settingsProvider.saveSettings();
	}
}
