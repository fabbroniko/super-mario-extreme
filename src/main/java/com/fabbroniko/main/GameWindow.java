package com.fabbroniko.main;

import javax.swing.*;

public final class GameWindow extends JFrame {

	private static final String GAME_NAME = "Super Mario Bros Extreme Edition";

	private final GamePanel gamePanel;

	public GameWindow(final GamePanel gamePanel) {
		this.setTitle(GAME_NAME);
		this.gamePanel = gamePanel;
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
