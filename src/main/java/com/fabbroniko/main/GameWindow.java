package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension;

import java.awt.*;

import javax.swing.JFrame;

public final class GameWindow extends JFrame {

	private static final String GAME_NAME = "Super Mario Bros Extreme Edition";
	private static final Dimension BASE_WINDOW_SIZE = new Dimension(320, 240);
	private static final double SCALE_DIVISION = 2;

	private final GamePanel gamePanel;

	public GameWindow() {
		final java.awt.Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		final double xScale = screenDimensions.getWidth() / BASE_WINDOW_SIZE.getWidth() / SCALE_DIVISION;
		final double yScale = screenDimensions.getHeight() / BASE_WINDOW_SIZE.getHeight() / SCALE_DIVISION;
		final Dimension windowDimension = new Dimension(
				(int) (BASE_WINDOW_SIZE.getWidth() * xScale),
				(int) (BASE_WINDOW_SIZE.getHeight() * yScale)
		);

		this.setTitle(GAME_NAME);
		this.gamePanel = new GamePanel(BASE_WINDOW_SIZE, windowDimension);
		this.setContentPane(gamePanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	public GamePanel getView() {
		return gamePanel;
	}
}
