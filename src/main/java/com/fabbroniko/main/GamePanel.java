package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.NullCustomKeyListener;
import lombok.Getter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public final class GamePanel extends JPanel implements GameCanvas {

	private final Dimension windowSize;
	@Getter
	private final Dimension2D canvasSize;
	private final BufferedImage canvasImage;
	@Getter
	private final Graphics2D canvas;
	private KeyListener keyListener;
	private JFrame window;

	public GamePanel(final Dimension2D canvasSize, final WindowSizeResolver windowSizeResolver) {
		super();

		this.canvasSize = canvasSize;
		this.windowSize = windowSizeResolver.dimension();
		this.keyListener = new NullCustomKeyListener();
		
		this.setPreferredSize(windowSize);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);

		canvasImage = new BufferedImage(canvasSize.width(), canvasSize.height(), BufferedImage.TYPE_INT_RGB);
		canvas = (Graphics2D) canvasImage.getGraphics();
	}

	@Override
	public void paintComponent(final Graphics cGraphics) {
		super.paintComponent(cGraphics);

		cGraphics.drawImage(canvasImage, 0, 0, (int) windowSize.getWidth(), (int) windowSize.getHeight(), null);
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

	@Override
	public void init() {
		window = new JFrame();
		window.setTitle("Super Mario Bros Extreme Edition");
		window.setContentPane(this);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}

	@Override
	public void draw(final BufferedImage frame) {
		canvas.drawImage(frame, null, 0, 0);
		repaint();
	}

	@Override
	public void close() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
}
