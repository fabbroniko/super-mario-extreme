package fabbroniko.scene;

import java.awt.*;
import java.awt.event.KeyEvent;

import fabbroniko.environment.Background;
import fabbroniko.environment.Dimension;

/**
 * Handles and Draws the Settings State.
 * @author fabbroniko
 *
 */
public final class SettingsMenuScene extends AbstractScene {

	private Background bg;

	private int currentSelection;
	private boolean keyListening;
	
	private static final Color WHITE = new Color(0xffffff);
	private static final Color RED = new Color(0xff0000);
	private static final int MAX_SELECTION = 4;
	private static final String RES_BACKGROUND_IMAGE = "/fabbroniko/Menu/BaseBG.png";
	
	private static final int ABSOLUTE_POSITION_30 = 30;
	private static final int ABSOLUTE_POSITION_60 = 60;
	private static final int ABSOLUTE_POSITION_90 = 90;
	private static final int ABSOLUTE_POSITION_120 = 120;
	private static final int ABSOLUTE_POSITION_150 = 150;
	private static final int ABSOLUTE_POSITION_200 = 200;
	
	private static final int SELECTION_LEFT_KEY = 0;
	private static final int SELECTION_RIGHT_KEY = 1;
	private static final int SELECTION_JUMP_KEY = 2;
	private static final int SELECTION_MUSIC = 3;
	private static final int SELECTION_EFFECTS = 4;

	@Override
	public void init() {
		bg = new Background(RES_BACKGROUND_IMAGE);
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		bg.draw(g, gDimension);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor((currentSelection == SELECTION_LEFT_KEY) ? RED : WHITE);
		g.drawString("Left Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_30);
		g.drawString("<" + getKeyString(gameManager.getSettings().getLeftMovementKeyCode()) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_30);
		
		g.setColor((currentSelection == SELECTION_RIGHT_KEY) ? RED : WHITE);
		g.drawString("Right Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_60);
		g.drawString("<" + getKeyString(gameManager.getSettings().getRightMovementKeyCode()) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_60);
		
		g.setColor((currentSelection == SELECTION_JUMP_KEY) ? RED : WHITE);
		g.drawString("Jump Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_90);
		g.drawString("<" + getKeyString(gameManager.getSettings().getJumpKeyCode()) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_90);
		
		g.setColor((currentSelection == SELECTION_MUSIC) ? RED : WHITE);
		g.drawString("Music: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_120);
		g.drawString("<" + (gameManager.getSettings().isMusicActive() ? "ON" : "OFF") + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_120);
		
		g.setColor((currentSelection == SELECTION_EFFECTS) ? RED : WHITE);
		g.drawString("Effects: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_150);
		g.drawString("<" + (gameManager.getSettings().isEffectsAudioActive() ? "ON" : "OFF") + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_150);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		super.keyPressed(e);

		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				gameManager.openScene(new MainMenuScene());
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
				keyHandler(e);
				break;
		}
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
			gameManager.getSettings().invertEffectActive();
		else if (currentSelection == SELECTION_MUSIC)
			gameManager.getSettings().invertMusicActive();
		else
			keyListening ^= true;
	}

	private void keyHandler(final KeyEvent e) {
		if(!keyListening)
			return;

		 keyHandler(e.getKeyCode());
	}

	private void keyHandler(final int keyCode) {
		if(!keyListening)
			return;

		if(currentSelection == SELECTION_LEFT_KEY) {
			gameManager.getSettings().setLeftMovementKeyCode(keyCode);
		} else if (currentSelection == SELECTION_RIGHT_KEY) {
			gameManager.getSettings().setRightMovementKeyCode(keyCode);
		} else if (currentSelection == SELECTION_JUMP_KEY) {
			gameManager.getSettings().setJumpKeyCode(keyCode);
		}
	}
}
