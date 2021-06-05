package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension;

import java.awt.*;

import javax.swing.JFrame;

public final class GameWindow extends JFrame {

	private static final String GAME_NAME = "Super Mario Bros Extreme Edition";
	private static final Dimension BASE_WINDOW_SIZE = new Dimension(1280, 960);

	private final GamePanel gamePanel;

	public GameWindow() {
		final java.awt.Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension windowDimension = new Dimension((int) screenDimensions.getWidth(), (int) screenDimensions.getHeight());

		this.setTitle(GAME_NAME);
		this.gamePanel = new GamePanel(BASE_WINDOW_SIZE, windowDimension);
		this.setContentPane(gamePanel);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	public GamePanel getView() {
		return gamePanel;
	}
}
