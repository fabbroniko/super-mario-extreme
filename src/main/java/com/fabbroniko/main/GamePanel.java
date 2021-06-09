package com.fabbroniko.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.fabbroniko.environment.Vector2D;

public final class GamePanel extends JPanel {

	private static final Vector2D origin = new Vector2D();

	private final Vector2D windowSize;
	private final Vector2D canvasSize;
	private final BufferedImage canvasImage;
	private final Graphics2D canvas;

	public GamePanel(final Vector2D canvasSize, final Vector2D windowSize) {
		super();

		this.canvasSize = canvasSize;
		this.windowSize = windowSize;
		
		this.setPreferredSize(new Dimension(windowSize.getRoundedX(), windowSize.getRoundedY()));
		this.setFocusable(true);
		this.requestFocus();

		canvasImage = new BufferedImage(canvasSize.getRoundedX(), canvasSize.getRoundedY(), BufferedImage.TYPE_INT_RGB);
		canvas = (Graphics2D) canvasImage.getGraphics();
	}

	public Graphics2D getCanvas() {
		return canvas;
	}

	@Override
	public void paintComponent(final Graphics cGraphics) {
		super.paintComponent(cGraphics);

		cGraphics.drawImage(canvasImage, origin.getRoundedX(), origin.getRoundedY(), windowSize.getRoundedX(), windowSize.getRoundedY(), null);
	}

	public Vector2D getCanvasSize() {
		return canvasSize.clone();
	}
}
