package fabbroniko.scene;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import fabbroniko.environment.AudioManager;
import fabbroniko.environment.Background;
import fabbroniko.environment.Dimension;
import fabbroniko.environment.Service;
import fabbroniko.environment.Position;
import fabbroniko.error.ResourceNotFoundError;
import fabbroniko.gamestatemanager.AbstractGameState;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.gamestatemanager.IGameStateManager.State;
import fabbroniko.main.Drawable;
import fabbroniko.main.KeyDependent;

/**
 * Handles and Draws the main menu.
 * @author fabbroniko
 *
 */
public final class MainMenuScene extends AbstractGameState {

	// Oggetti da disegnare sullo schermo.
	private Background bg;				// Background
	private BufferedImage titleImage;
	private BufferedImage baseImage;	// Immagine di base di un'opzione del menu.
	private BufferedImage startImage;
	private BufferedImage settingsImage;
	private BufferedImage quitImage;
	private BufferedImage startImageS;
	private BufferedImage settingsImageS;
	private BufferedImage quitImageS;
	
	// Options position & dimension
	private Position titlePosition;
	private Position startOption;
	private Position settingsOption;
	private Position quitOption;

	private static final MainMenuScene MY_INSTANCE = new MainMenuScene();
	
	// Resources
	private static final String RES_BG_IMAGE = "/fabbroniko/Menu/BaseBG.png";
	private static final String RES_MENU_BASE = "/fabbroniko/Menu/BaseOption.png";
	private static final String RES_TITLE_IMAGE = "/fabbroniko/Menu/Title.png";
	private static final String RES_START_IMAGE = "/fabbroniko/Menu/StartString.png";
	private static final String RES_START_IMAGE_SELECTED = "/fabbroniko/Menu/StartSelected.png";
	private static final String RES_SETTINGS_IMAGE = "/fabbroniko/Menu/SettingsString.png";
	private static final String RES_SETTINGS_IMAGE_SELECTED = "/fabbroniko/Menu/SettingsSelected.png";
	private static final String RES_QUIT_IMAGE = "/fabbroniko/Menu/QuitString.png";
	private static final String RES_QUIT_IMAGE_SELECTED = "/fabbroniko/Menu/QuitSelected.png";
	
	private static final int BOTTOM_OFFSET = 30;
	private static final int OPTIONS_DISTACE = 20;
	
	private static final int START_OPTION = 0;
	private static final int SETTINGS_OPTION = 1;
	private static final int QUIT_OPTION = 2;
	
	private int selectedOption;
	
	/**
	 * Constructs a new MenuState
	 * @param gsm Reference of the GameStateManager
	 */
	private MainMenuScene() {
		super();
	}

	/**
	 * Gets the single instance of this class.
	 * @return The single instance of this class.
	 */
	public static MainMenuScene getInstance() {
		return MY_INSTANCE;
	}

	/**
	 * @see AbstractGameState#init()
	 */
	@Override
	public void init() {
		String tmpImage = "";
		
		bg = new Background(RES_BG_IMAGE);
		selectedOption = 0;
		
		// Inizializzazione immagine di base.
		try {
			tmpImage = RES_TITLE_IMAGE;
			titleImage = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_MENU_BASE;
			baseImage = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_START_IMAGE;
			startImage = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_SETTINGS_IMAGE;
			settingsImage = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_QUIT_IMAGE;
			quitImage = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_START_IMAGE_SELECTED;
			startImageS = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_SETTINGS_IMAGE_SELECTED;
			settingsImageS = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			tmpImage = RES_QUIT_IMAGE_SELECTED;
			quitImageS = ImageIO.read(getClass().getResourceAsStream(tmpImage));
			
		} catch (Exception e) {
			throw new ResourceNotFoundError(tmpImage);
		}
		
		Dimension baseImageDimension;
		Dimension titleDimension;
		
		baseImageDimension = new Dimension(baseImage.getWidth(), baseImage.getHeight());
		titleDimension = new Dimension(titleImage.getWidth(), titleImage.getHeight());
		
		quitOption = Service.getXCentredPosition(baseImageDimension);
		quitOption.setY((int) (GameStateManager.getInstance().getBaseWindowSize().getHeight() - (BOTTOM_OFFSET + baseImageDimension.getHeight())));
		
		settingsOption = Service.getXCentredPosition(baseImageDimension);
		settingsOption.setY(quitOption.getY() - (OPTIONS_DISTACE + baseImageDimension.getHeight()));
		
		startOption = Service.getXCentredPosition(baseImageDimension);
		startOption.setY(settingsOption.getY() - (OPTIONS_DISTACE + baseImageDimension.getHeight()));
		
		titlePosition = Service.getXCentredPosition(titleDimension);
		titlePosition.setY(startOption.getY() - (OPTIONS_DISTACE + titleDimension.getHeight()));
		
		AudioManager.getInstance().stopCurrent();
	}

	/**
	 * @see Drawable#draw(Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g, final Dimension gDimension) {		
		BufferedImage start = null;
		BufferedImage settings = null;
		BufferedImage quit = null;
		
		bg.draw(g, gDimension);
		
		switch(selectedOption) {
		case START_OPTION:
			start = startImageS;
			settings = settingsImage;
			quit = quitImage;
			break;
		case SETTINGS_OPTION:
			start = startImage;
			settings = settingsImageS;
			quit = quitImage;
			break;
		case QUIT_OPTION:
			start = startImage;
			settings = settingsImage;
			quit = quitImageS;
			break;
		default:
			break;
		}
		
		g.drawImage(titleImage, titlePosition.getX(), titlePosition.getY(), null);
		
		g.drawImage(baseImage, startOption.getX(), startOption.getY(), null);
		g.drawImage(start, startOption.getX(), startOption.getY(), null);
		
		g.drawImage(baseImage, settingsOption.getX(), settingsOption.getY(), null);
		g.drawImage(settings, settingsOption.getX(), settingsOption.getY(), null);
		
		g.drawImage(baseImage, quitOption.getX(), quitOption.getY(), null);
		g.drawImage(quit, quitOption.getX(), quitOption.getY(), null);
	}

	/**
	 * @see KeyDependent#keyPressed(KeyEvent)
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
			if (selectedOption == START_OPTION) {
				GameStateManager.getInstance().setState(State.LEVEL1_STATE);
			} else if (selectedOption == SETTINGS_OPTION) {
				GameStateManager.getInstance().setState(State.SETTINGS_STATE);
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
		if (selectedOption < START_OPTION) {
			selectedOption = QUIT_OPTION;
		} else if (selectedOption > QUIT_OPTION) {
			selectedOption = START_OPTION;
		}
	}
}
