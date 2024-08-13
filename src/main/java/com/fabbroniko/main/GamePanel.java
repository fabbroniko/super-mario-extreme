package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.ControlsSource;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public final class GamePanel extends JPanel implements ControlsSource, KeyListener {

	private final Dimension2D windowSize;
	@Getter
	private final Dimension2D canvasSize;
	private final BufferedImage canvasImage;
	@Getter
	private final Graphics2D canvas;
	private KeyListener keyListener;

	public GamePanel(final Dimension2D canvasSize, final Dimension2D windowSize) {
		super();

		this.canvasSize = canvasSize;
		this.windowSize = windowSize;
		this.keyListener = new NullCustomKeyListener();
		
		this.setPreferredSize(new Dimension(windowSize.width(), windowSize.height()));
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);

		canvasImage = new BufferedImage(canvasSize.width(), canvasSize.height(), BufferedImage.TYPE_INT_RGB);
		canvas = (Graphics2D) canvasImage.getGraphics();
	}

	@Override
	public void paintComponent(final Graphics cGraphics) {
		super.paintComponent(cGraphics);

		cGraphics.drawImage(canvasImage, 0, 0, windowSize.width(), windowSize.height(), null);
	}

	@Override
	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		keyListener.keyPressed(event);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keyListener.keyReleased(event);
	}
}
