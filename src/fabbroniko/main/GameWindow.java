package fabbroniko.main;

import fabbroniko.environment.Dimension;

import java.awt.Toolkit;

import javax.swing.JFrame;


/**
 * Starts a new game.
 * @author fabbroniko 
 *
 */
public final class GameWindow extends JFrame {

	private static final String GAME_NAME = "Super Mario Bros Extreme Edition";

	private static final double SCALE_DIVISION = 2;
	private static final int FPS = 50;
	
	private static final Dimension BASE_WINDOW_SIZE = new Dimension(320, 240);
	private static final Dimension SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	private static final double X_SCALE = (double) (SCREEN_SIZE.getWidth() / BASE_WINDOW_SIZE.getWidth() / SCALE_DIVISION);
	private static final double Y_SCALE = (double) (SCREEN_SIZE.getHeight() / BASE_WINDOW_SIZE.getHeight() / SCALE_DIVISION);
	private static final Dimension ACTUAL_WINDOW_SIZE = new Dimension((int) (BASE_WINDOW_SIZE.getWidth() * X_SCALE), (int) (BASE_WINDOW_SIZE.getHeight() * Y_SCALE));
	
	/**
	 * Time for which every frame should stay on the screen.
	 */
	public static final int FPS_MILLIS = 1000 / FPS;
	
	/**
	 * Contructs the whole game
	 */
	public GameWindow() {
		this.setTitle(GAME_NAME);
		this.setContentPane(new GamePanel(BASE_WINDOW_SIZE, ACTUAL_WINDOW_SIZE));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}
}
