package com.fabbroniko.main;

import com.fabbroniko.environment.Vector2D;

import javax.swing.JFrame;
import java.awt.Toolkit;

public final class GameWindow extends JFrame {

	private static final String GAME_NAME = "Super Mario Bros Extreme Edition";
	private static final Vector2D BASE_WINDOW_SIZE = new Vector2D(1280, 960);

	private final GamePanel gamePanel;

	public GameWindow() {
		this.setTitle(GAME_NAME);
		this.gamePanel = new GamePanel(BASE_WINDOW_SIZE);
		this.setContentPane(gamePanel);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	public GamePanel getView() {
		return gamePanel;
	}
}
