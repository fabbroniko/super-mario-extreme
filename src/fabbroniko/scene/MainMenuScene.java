package fabbroniko.scene;

import java.awt.*;
import java.awt.event.KeyEvent;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Background;
import fabbroniko.environment.Dimension;
import fabbroniko.gamestatemanager.AbstractScene;
import fabbroniko.gamestatemanager.GameStateManager;

public final class MainMenuScene extends AbstractScene {

	// Strings
	private static final String TITLE = "Super Mario Extreme Edition";
	private static final String START_GAME_OPTION = "Start";
	private static final String SETTINGS_OPTION = "Settings";
	private static final String QUIT_OPTION = "Quit";

	// Resources
	private static final String RES_BG_IMAGE = "/fabbroniko/Menu/BaseBG.png";

	// Indexes
	private static final int START_OPTION_INDEX = 0;
	private static final int SETTINGS_OPTION_INDEX = 1;
	private static final int QUIT_OPTION_INDEX = 2;

	private Background bg;
	private int selectedOption;

	@Override
	public void init() {
		bg = new Background(RES_BG_IMAGE);
		selectedOption = 0;
		
		AudioManager.getInstance().stopCurrent();
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		bg.draw(g, gDimension);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.GREEN);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 20));
		int centeredXPosition = getCenteredXPositionForString(TITLE, g, gDimension);
		g.drawString(TITLE, centeredXPosition, 30);

		printStartMenuOption(g, gDimension, selectedOption == START_OPTION_INDEX);
		printSettingsMenuOption(g, gDimension, selectedOption == SETTINGS_OPTION_INDEX);
		printQuitMenuOption(g, gDimension, selectedOption == QUIT_OPTION_INDEX);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
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
				GameStateManager.getInstance().openScene(new GameScene());
			} else if (selectedOption == SETTINGS_OPTION_INDEX) {
				GameStateManager.getInstance().openScene(new SettingsMenuScene());
			} else {
				GameStateManager.getInstance().exit();
			}
			break;
		case KeyEvent.VK_ESCAPE:
			GameStateManager.getInstance().exit();
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

	private void printStartMenuOption(final Graphics2D g, final Dimension canvasDimension, final boolean isSelected) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawRoundRect(getCenteredXPositionFromSize(canvasDimension, 90), 80, 90, 30, 8, 8);

		Color textColor = Color.BLACK;
		if(isSelected)
			textColor = Color.GREEN;

		g.setColor(textColor);
		g.setFont(g.getFont().deriveFont(Font.PLAIN, 20));

		g.drawString(START_GAME_OPTION, getCenteredXPositionForString(START_GAME_OPTION, g, canvasDimension), 103);
	}

	private void printSettingsMenuOption(final Graphics2D g, final Dimension canvasDimension, final boolean isSelected) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawRoundRect(getCenteredXPositionFromSize(canvasDimension, 90), 130, 90, 30, 8, 8);

		Color textColor = Color.BLACK;
		if(isSelected)
			textColor = Color.GREEN;

		g.setColor(textColor);
		g.setFont(g.getFont().deriveFont(Font.PLAIN, 20));

		g.drawString(SETTINGS_OPTION, getCenteredXPositionForString(SETTINGS_OPTION, g, canvasDimension), 153);
	}

	private void printQuitMenuOption(final Graphics2D g, final Dimension canvasDimension, final boolean isSelected) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawRoundRect(getCenteredXPositionFromSize(canvasDimension, 90), 180, 90, 30, 8, 8);

		Color textColor = Color.BLACK;
		if(isSelected)
			textColor = Color.GREEN;

		g.setColor(textColor);
		g.setFont(g.getFont().deriveFont(Font.PLAIN, 20));

		g.drawString(QUIT_OPTION, getCenteredXPositionForString(QUIT_OPTION, g, canvasDimension), 203);
	}
}
