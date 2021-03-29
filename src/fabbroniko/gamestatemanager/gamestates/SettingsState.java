package fabbroniko.gamestatemanager.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fabbroniko.environment.Background;
import fabbroniko.environment.Dimension;
import fabbroniko.gamestatemanager.AbstractGameState;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.gamestatemanager.IGameStateManager.State;

/**
 * Handles and Draws the Settings State.
 * @author fabbroniko
 *
 */
public final class SettingsState extends AbstractGameState {

	private Background bg;
	
	private static final SettingsState MY_INSTANCE = new SettingsState();
	private int currentSelection;
	private boolean keyListening;
	private boolean leftKeyListening;
	private boolean rightKeyListening;
	private boolean jumpKeyListening;
	
	private int jumpKeyCode = KeyEvent.VK_SPACE;
	private int rightKeyCode = KeyEvent.VK_RIGHT;
	private int leftKeyCode = KeyEvent.VK_LEFT;
	private boolean music = true;
	private boolean effects = true;
	
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
	
	/**
	 * Constructs a new SettingsState
	 * @param gsm Reference of the GameStateManager
	 */
	private SettingsState() {
		super();
	}
	
	/**
	 * Gets the single instance of this class.
	 * @return The single instance of this class.
	 */
	public static SettingsState getInstance() {
		return MY_INSTANCE;
	}

	@Override
	public void init() {
		bg = new Background(RES_BACKGROUND_IMAGE);
	}

	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {
		bg.draw(g, gDimension);
		g.setColor((currentSelection == SELECTION_LEFT_KEY) ? RED : WHITE);
		g.drawString("Left Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_30);
		g.drawString("<" + getKeyString(leftKeyCode) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_30);
		
		g.setColor((currentSelection == SELECTION_RIGHT_KEY) ? RED : WHITE);
		g.drawString("Right Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_60);
		g.drawString("<" + getKeyString(rightKeyCode) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_60);
		
		g.setColor((currentSelection == SELECTION_JUMP_KEY) ? RED : WHITE);
		g.drawString("Jump Key: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_90);
		g.drawString("<" + getKeyString(jumpKeyCode) + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_90);
		
		g.setColor((currentSelection == SELECTION_MUSIC) ? RED : WHITE);
		g.drawString("Music: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_120);
		g.drawString("<" + (music ? "ON" : "OFF") + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_120);
		
		g.setColor((currentSelection == SELECTION_EFFECTS) ? RED : WHITE);
		g.drawString("Effects: ", ABSOLUTE_POSITION_30, ABSOLUTE_POSITION_150);
		g.drawString("<" + (effects ? "ON" : "OFF") + ">", ABSOLUTE_POSITION_200, ABSOLUTE_POSITION_150);
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
	
	/**
	 * Gets the actual Jump Key.
	 * @return Jump Key Value.
	 */
	public int getJumpKey() {
		return this.jumpKeyCode;
	}
	
	/**
	 * Gets the actual Right Key.
	 * @return Right Key Value.
	 */
	public int getRightKey() {
		return this.rightKeyCode;
	}
	
	/**
	 * Gets the actual Left Key.
	 * @return Left Key Value.
	 */
	public int getLeftKeyCode() {
		return leftKeyCode;
	}
	
	/**
	 * Gets whether music is allowed or not.
	 * @return True if the background can be played, and false otherwise
	 */
	public boolean musicIsActive() {
		return music;
	}
	
	/**
	 * Gets whether effects are allowed or not.
	 * @return True if effects can be played, and false otherwise
	 */
	public boolean effectIsActive() {
		return effects;
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		super.keyPressed(e);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameStateManager.getInstance().setState(State.MENU_STATE);
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
				music ^= true;
				break;
			case SELECTION_EFFECTS:
				effects ^= true;
				break;
			default:
					break;
			}
		}
		
		if (leftKeyListening) {
			leftKeyCode = e.getKeyCode();
		} else if (rightKeyListening) {
			rightKeyCode = e.getKeyCode();
		} else if (jumpKeyListening) {
			jumpKeyCode = e.getKeyCode();
		}
	}
}
