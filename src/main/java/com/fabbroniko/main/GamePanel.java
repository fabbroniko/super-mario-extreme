package com.fabbroniko.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.Position;
import com.fabbroniko.gamestatemanager.GameManager;
import com.fabbroniko.scene.MainMenuScene;

/**
 * Panel where the game will be drawn.
 * @author nicola.fabbrini
 */
public final class GamePanel extends JPanel implements Runnable, IView {

	private final Position origin = new Position();

	private GameManager gameManager;
	private Thread gameThread;							// Thread che conterrà la gestione di ogni parte del gioco.
	private boolean running;					// Campo booleano che serve ad uscire dal game loop.
	private BufferedImage image;						// Buffer dell'immagine finale che dovrà essere visualizzata sullo schermo
	
	private boolean threadInitialized;
	
	private Dimension baseWindowSize;
	private Dimension actualWindowSize;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes the game panel.
	 */
	public GamePanel(final Dimension baseWindowSize, final Dimension actualWindowSize) {
		super();
		
		this.baseWindowSize = baseWindowSize;
		this.actualWindowSize = actualWindowSize;
		
		this.setPreferredSize(new java.awt.Dimension(actualWindowSize.getWidth(), actualWindowSize.getHeight()));
		this.setFocusable(true);
		this.requestFocus();
		
		gameManager = GameManager.setInstance(this);
		gameManager.openScene(new MainMenuScene());
	}
	
	/**
	 * @see JPanel#addNotify()
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		if (!threadInitialized) {
			gameThread = new Thread(this);
			gameThread.start();
			threadInitialized = true;
		}
	}

	@Override
	public void run() {
		long currentTime;
		long wait;

		running = true;
		image = new BufferedImage(baseWindowSize.getWidth(), baseWindowSize.getHeight(), BufferedImage.TYPE_INT_RGB);
		final Graphics2D imgGraphics = (Graphics2D) image.getGraphics();

		// Game Loop
		while (running) {

			currentTime = System.currentTimeMillis();

			this.update();
			this.draw(imgGraphics);
			this.repaint();

			wait = GameWindow.FPS_MILLIS - (System.currentTimeMillis() - currentTime);
			if (wait < 0) { 
				wait = 0; 
			}

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				System.out.println("Error occurred trying to call Thread.sleep.");
			}
		}
		System.exit(0);
	}

	/**
	 * @see Drawable#update()
	 */
	private void update() {
		GameManager.getInstance().update();
	}
	
	@Override
	public void exit() {
		running = false;
	}

	private void draw(final Graphics2D g) {
		GameManager.getInstance().draw(g, baseWindowSize);
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(Graphics)
	 */
	@Override
	public void paintComponent(final Graphics cGraphics) {
		super.paintComponent(cGraphics);
		cGraphics.drawImage(image, origin.getX(), origin.getY(), actualWindowSize.getWidth(), actualWindowSize.getHeight(), null);
	}
	
	/**
	 * Checks if the game is still running.
	 * @return The current state of the game. True if running. Otherwise, false.
	 */
	public boolean isRunning() {
		return running;
	}

	@Override
	public Dimension getBaseWindowSize() {
		return this.baseWindowSize;
	}
}