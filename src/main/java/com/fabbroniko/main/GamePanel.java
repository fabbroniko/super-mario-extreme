package com.fabbroniko.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.fabbroniko.environment.Dimension;
import com.fabbroniko.environment.Vector2D;

public final class GamePanel extends JPanel {

	private static final Vector2D origin = new Vector2D();

	private final Dimension windowSize;
	private final Dimension canvasSize;
	private final BufferedImage canvasImage;
	private final Graphics2D canvas;

	public GamePanel(final Dimension canvasSize, final Dimension windowSize) {
		super();

		this.canvasSize = canvasSize;
		this.windowSize = windowSize;
		
		this.setPreferredSize(new java.awt.Dimension(windowSize.getWidth(), windowSize.getHeight()));
		this.setFocusable(true);
		this.requestFocus();

		canvasImage = new BufferedImage(canvasSize.getWidth(), canvasSize.getHeight(), BufferedImage.TYPE_INT_RGB);
		canvas = (Graphics2D) canvasImage.getGraphics();
	}

	public Graphics2D getCanvas() {
		return canvas;
	}

	@Override
	public void paintComponent(final Graphics cGraphics) {
		super.paintComponent(cGraphics);

		cGraphics.drawImage(canvasImage, origin.getRoundedX(), origin.getRoundedY(), windowSize.getWidth(), windowSize.getHeight(), null);
	}

	public Dimension getCanvasSize() {
		return canvasSize;
	}
}
