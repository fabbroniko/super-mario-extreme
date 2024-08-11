package com.fabbroniko.scene;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Background;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContext;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.main.SettingsProvider;
import com.fabbroniko.resource.ResourceManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public final class SettingsMenuScene extends AbstractScene implements KeyListener, Scene {

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
	private Background bg;
	private int currentlyDrawingOption;
	private int currentSelection;
	private boolean keyListening;
	private boolean isClosed = false;

	private final SceneContextFactory sceneContextFactory;
	private final AudioManager audioManager;
	private final SettingsProvider settingsProvider;
	private final ResourceManager resourceManager;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public SettingsMenuScene(final SceneContextFactory sceneContextFactory,
							 final SettingsProvider settingsProvider,
							 final AudioManager audioManager,
							 final ResourceManager resourceManager) {

		this.sceneContextFactory = sceneContextFactory;
		this.settingsProvider = settingsProvider;
		this.audioManager = audioManager;
		this.resourceManager = resourceManager;
	}

	@Override
	public void init() {
		bg = new Background(resourceManager, "menu");

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.getSceneCanvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.getCanvasDimension();
	}

	@Override
	public void update() {}

	@Override
	public BufferedImage draw() {
		final Vector2D bgPosition = bg.getDrawingPosition();
		graphics.drawImage(bg.getDrawableImage(), bgPosition.getRoundedX(), bgPosition.getRoundedY(), canvasDimension.getWidth(), canvasDimension.getHeight(), null);

		// Setting up the shared parameters to all options
		graphics.setFont(P_FONT);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		/* Printing the options into the screen. The currently drawing option field is used to automatically print
		 * a option in a new line every time the printOption method is called. This is also used to check if the option
		 * we are currently drawing is also the one currently selected by the user.
		 */
		currentlyDrawingOption = 1;
		printOption("Left Key: ", settingsProvider.getSettings().getLeftMovementKeyCode(), graphics);
		printOption("Right Key: ", settingsProvider.getSettings().getRightMovementKeyCode(), graphics);
		printOption("Jump Key: ", settingsProvider.getSettings().getJumpKeyCode(), graphics);
		printOption("Music: ", settingsProvider.getSettings().isMusicActive(), graphics);
		printOption("Effects: ", settingsProvider.getSettings().isEffectsAudioActive(), graphics);
		printOption("Show FPS: ", settingsProvider.getSettings().isShowFps(), graphics);
		printOption("FPS Cap: ", String.valueOf(settingsProvider.getSettings().getFpsCap()), graphics);

		// Setting up the configuration for the bottom page hints.
		graphics.setColor(Color.WHITE);
		graphics.setFont(P_S_FONT);

		// Printing a hint depending on what the user is doing.
		int x;
		String hint;
		if(!keyListening) {
			x = getCenteredXPositionForString(HINT_1, graphics, canvasDimension);
			hint = HINT_1;
		} else {
			x = getCenteredXPositionForString(HINT_2, graphics, canvasDimension);
			hint = HINT_2;
		}

		// Draw the hint
		graphics.drawString(hint, x, HINT_Y);

		// Disabling antialiasing
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		return canvas;
	}

	@Override
	public void detach() {
		audioManager.stopMusic();
	}

	@Override
	public boolean isClosed() {
		return isClosed;
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE: // ESC
				isClosed = true;
				break;
			case KeyEvent.VK_UP: // Arrow UP
				specialKeyUpHandler();
				break;
			case KeyEvent.VK_DOWN: // Arrow DOWN
				specialKeyDownHandler();
				break;
			case KeyEvent.VK_ENTER: // ENTER
				specialKeyEnterHandler();
				break;
			default: // Any other key
				keyHandler(e.getKeyCode());
				break;
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {}

	@Override
	public void keyTyped(final KeyEvent e) {}

	/**
	 * Prints a setting name in the screen in a new line into the specified canvas.
	 * Used for key bindings.
	 *
	 * @param optionName The name of the option
	 * @param keyCode The key code of the key associated to this setting
	 * @param g The canvas we draw on
	 */
	private void printOption(final String optionName,
							 final int keyCode,
							 final Graphics2D g) {

		printOption(optionName, getKeyString(keyCode), g);
	}

	/**
	 * Print a setting name in the screen in a new line into the specified canvas.
	 * Used for ON/OFF options
	 *
	 * @param optionName The name of the option
	 * @param optionValue Whether the setting is ON (true) or OFF (false)
	 * @param g The canvas we draw on
	 */
	private void printOption(final String optionName,
							 final boolean optionValue,
							 final Graphics2D g) {

		final String sOptionValue = optionValue ? "ON" : "OFF";

		printOption(optionName, sOptionValue, g);
	}

	/**
	 * Print a setting name in the screen in a new line into the specified canvas.
	 *
	 * @param optionName The name of the option
	 * @param optionValue The value of the option
	 * @param g The canvas we draw on
	 */
	private void printOption(final String optionName,
							 final String optionValue,
							 final Graphics2D g) {

		Color textColor = Color.WHITE;
		if((currentSelection + 1) == currentlyDrawingOption)
			textColor = Color.GREEN;

		g.setColor(textColor);

		int y = FIRST_OPTION_Y + (OPTIONS_Y_OFFSET * currentlyDrawingOption);
		g.drawString(optionName, OPTION_NAME_X, y);
		g.drawString(optionValue, OPTION_VALUE_X, y);

		currentlyDrawingOption++;
	}

	/**
	 * Converts a key code into its string representation.
	 * This is used to convert the key code in a user friendly string displayed next to each option.
	 *
	 * @param keyCode The key code to convert
	 * @return The string value associated to the key code.
	 */
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

	/**
	 * The up button is used to scroll between options in the settings menu unless we are listening for a key to
	 * be entered. In that case we delegate the operation to the keyHandler method.
	 */
	private void specialKeyUpHandler() {
		if(!keyListening) {
			currentSelection--;
			currentSelection = currentSelection < 0 ? MAX_SELECTION : currentSelection;
			return;
		}

		keyHandler(KeyEvent.VK_UP);
	}

	/**
	 * The down button is used to scroll between options in the settings menu unless we are listening for a key to
	 * be entered. In that case we delegate the operation to the keyHandler method.
	 */
	private void specialKeyDownHandler() {
		if (!keyListening) {
			currentSelection++;
			currentSelection = currentSelection > MAX_SELECTION ? 0 : currentSelection;
			return;
		}

		keyHandler(KeyEvent.VK_DOWN);
	}

	/**
	 * Handles the ENTER Key.
	 * If one of the ON/OFF options is selected it simply swaps between the 2 options
	 * otherwise invert whatever value keyListening has (either confirm a key selection or start typing the new selection)
	 */
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

	/**
	 * Saves the pressed key into the user settings object depending on what option is currently selected.
	 * @param keyCode The key code to save into the user settings.
	 */
	private void keyHandler(final int keyCode) {
		// Ignore the call if we are currently not listening for a user input  - this should never happen
		if(!keyListening)
			return;

		// Checks what option is currently selected and saves the key code to the appropriate user setting variable.
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
