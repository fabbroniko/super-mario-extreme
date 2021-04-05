package fabbroniko.scene;

import java.awt.*;
import java.awt.event.KeyEvent;

import fabbroniko.environment.Background;
import fabbroniko.environment.Dimension;

/**
 * The main menu shown as the first scene of the game.
 * Here the user can select to either play, customize some settings or exit the game
 *
 * When an option is selected it's drawn in a different color to give the user a visual feedback of what's going on.
 */
public final class MainMenuScene extends AbstractScene {

	// Strings
	private static final String TITLE = "Super Mario Extreme Edition";
	private static final String START_GAME_OPTION = "Start";
	private static final String SETTINGS_OPTION = "Settings";
	private static final String QUIT_OPTION = "Quit";
	private static final String HINT = "Arrow UP/DOWN to navigate. ENTER to confirm.";

	// Resources
	private static final String RES_BG_IMAGE = "/fabbroniko/Menu/BaseBG.png";

	// Indexes
	private static final int START_OPTION_INDEX = 0;
	private static final int SETTINGS_OPTION_INDEX = 1;
	private static final int QUIT_OPTION_INDEX = 2;

	// Magic numbers
	private static final int STROKE_SIZE = 3;
	private static final int OPTION_ARC_SIZE = 8;
	private static final Dimension OPTION_RECTANGLE_DIMENSION = new Dimension(90, 30);
	private static final int OPTION_RECT_TO_TEXT_OFFSET = 23;
	private static final int TITLE_Y = 30;
	private static final int START_OPTION_Y = 65;
	private static final int OPTIONS_OFFSET = 50;
	private static final int HINT_Y = 230;

	private Background bg;

	// Allows the program to keep track of what option is currently selected. This is used to execute an operation when
	// the player hits ENTER and it allows the draw function to draw the option in a different color.
	private int selectedOption;

	/**
	 * Loads the resources loaded for this scene.
	 */
	@Override
	public void init() {
		bg = new Background(RES_BG_IMAGE);
		selectedOption = 0;
	}

	/**
	 * Handles what's gonna be drawn in the menu.
	 *
	 * The Background is drawn first as a first layer while the title and menu options are drawn after (resulting on drawing to top of the background)
	 * @param g The canvas where the menu is drawn
	 * @param canvasDimension The dimension of the canvas
	 */
	@Override
	public void draw(final Graphics2D g, final Dimension canvasDimension) {
		// Draw the background first
		bg.draw(g, canvasDimension);

		// Activating antialiasing to soften up the look of the strings
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Setting up the parameters to draw the game title
		g.setColor(Color.GREEN);
		g.setFont(H1_FONT);
		int centeredXPosition = getCenteredXPositionForString(TITLE, g, canvasDimension);

		// Draw the game title
		g.drawString(TITLE, centeredXPosition, TITLE_Y);

		// Print the menu options
		printMenuOption(START_GAME_OPTION, START_OPTION_Y, g, canvasDimension, selectedOption == START_OPTION_INDEX);
		printMenuOption(SETTINGS_OPTION, START_OPTION_Y + OPTIONS_OFFSET, g, canvasDimension, selectedOption == SETTINGS_OPTION_INDEX);
		printMenuOption(QUIT_OPTION, START_OPTION_Y + 2*OPTIONS_OFFSET, g, canvasDimension, selectedOption == QUIT_OPTION_INDEX);

		// Setting up the configuration for the bottom page hints.
		g.setColor(Color.WHITE);
		g.setFont(P_S_FONT);
		int x = getCenteredXPositionForString(HINT, g, canvasDimension);

		// Draw the hint
		g.drawString(HINT, x, HINT_Y);

		// Disabling antialiasing to make sure the next cycle doesn't apply it to the background or whatever is rendered next
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	/**
	 * Handles the keys pressed by the player.
	 *
	 * The allowed keys for this scene are:
	 * Arrow UP to move the menu selection upwards
	 * Arrow DOWN to move the menu selection downwards
	 * Enter to open the selected option
	 * ESC to close the game.
	 * @param e The key event that triggered this call
	 */
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
				gameManager.openScene(new GameScene());
			} else if (selectedOption == SETTINGS_OPTION_INDEX) {
				gameManager.openScene(new SettingsMenuScene());
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

	/**
	 * Delegates the operation of drawing the a option to this method.
	 * It will take care of drawing the rounded rectangle around the option name, the color in case it's currently selected
	 * and the option name itself.
	 *
	 * @param g The canvas where the option will be drawn.
	 * @param canvasDimension The dimension of the canvas.
	 * @param isSelected Whether the option is currently selected or not
	 * @param text The option name displayed within the rounded box
	 * @param y The y origin coordinate of the option box
	 */
	private void printMenuOption(final String text, final int y, final Graphics2D g, final Dimension canvasDimension, final boolean isSelected) {
		// Sets up the requirements to draw the rounded rectangle
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(STROKE_SIZE));
		int x = getCenteredXPositionFromSize(canvasDimension, OPTION_RECTANGLE_DIMENSION.getWidth());

		// Draws the rectangle
		g.drawRoundRect(x, y, OPTION_RECTANGLE_DIMENSION.getWidth(), OPTION_RECTANGLE_DIMENSION.getHeight(), OPTION_ARC_SIZE, OPTION_ARC_SIZE);

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
