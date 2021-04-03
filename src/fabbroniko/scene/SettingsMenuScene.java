package fabbroniko.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fabbroniko.Settings;
import fabbroniko.environment.Background;
import fabbroniko.environment.Dimension;
import fabbroniko.gamestatemanager.GameManager;

/**
 * Handles and Draws the Settings State.
 * @author fabbroniko
 *
 */
public final class SettingsMenuScene extends AbstractScene {

	private Background bg;

	private int currentSelection;
	private boolean keyListening;
	private boolean leftKeyListening;
	private boolean rightKeyListening;
	private boolean jumpKeyListening;
	
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
		g.setColor((currentSelection == SELECTION_LEFT_KEY) ? RED : WHITE);
		g.drawString("Left Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_30);
		g.drawString("<" + getKeyString(Settings.GLOBAL_SETTINGS.getLeftMovementKeyCode()) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_30);
		
		g.setColor((currentSelection == SELECTION_RIGHT_KEY) ? RED : WHITE);
		g.drawString("Right Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_60);
		g.drawString("<" + getKeyString(Settings.GLOBAL_SETTINGS.getRightMovementKeyCode()) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_60);
		
		g.setColor((currentSelection == SELECTION_JUMP_KEY) ? RED : WHITE);
		g.drawString("Jump Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_90);
		g.drawString("<" + getKeyString(Settings.GLOBAL_SETTINGS.getJumpKeyCode()) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_90);
		
		g.setColor((currentSelection == SELECTION_MUSIC) ? RED : WHITE);
		g.drawString("Music: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_120);
		g.drawString("<" + (Settings.GLOBAL_SETTINGS.isMusicActive() ? "ON" : "OFF") + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_120);
		
		g.setColor((currentSelection == SELECTION_EFFECTS) ? RED : WHITE);
		g.drawString("Effects: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_150);
		g.drawString("<" + (Settings.GLOBAL_SETTINGS.isEffectsAudioActive() ? "ON" : "OFF") + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_150);
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

	@Override
	public void keyPressed(final KeyEvent e) {
		super.keyPressed(e);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameManager.getInstance().openScene(new MainMenuScene());
		}
		if (e.getKeyCode() == KeyEvent.VK_UP && !keyListening) {
			currentSelection--;
			currentSelection = currentSelection < 0 ? MAX_SELECTION : currentSelection;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN && !keyListening) {
			currentSelection++;
			currentSelection = currentSelection > MAX_SELECTION ? 0 : currentSelection;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			switch(currentSelection) {
			case SELECTION_LEFT_KEY:
				keyListening ^= true;
				leftKeyListening ^= true;
				break;
			case SELECTION_RIGHT_KEY: 
				keyListening ^= true;
				rightKeyListening ^= true;
				break;
			case SELECTION_JUMP_KEY:
				keyListening ^= true;
				jumpKeyListening ^= true;
				break;
			case SELECTION_MUSIC:
				Settings.GLOBAL_SETTINGS.setMusicActive(!Settings.GLOBAL_SETTINGS.isMusicActive());
				break;
			case SELECTION_EFFECTS:
				Settings.GLOBAL_SETTINGS.setEffectsAudioActive(!Settings.GLOBAL_SETTINGS.isEffectsAudioActive());
				break;
			default:
					break;
			}
		}
		
		if (leftKeyListening) {
			Settings.GLOBAL_SETTINGS.setLeftMovementKeyCode(e.getKeyCode());
		} else if (rightKeyListening) {
			Settings.GLOBAL_SETTINGS.setRightMovementKeyCode(e.getKeyCode());
		} else if (jumpKeyListening) {
			Settings.GLOBAL_SETTINGS.setJumpKeyCode(e.getKeyCode());
		}
	}
}
