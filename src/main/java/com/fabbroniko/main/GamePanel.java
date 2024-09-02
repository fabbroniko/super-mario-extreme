package com.fabbroniko.main;

import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.NullCustomKeyListener;
import lombok.Getter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public final class GamePanel implements GameRenderer, KeyListener {

	private final Dimension windowSize;
	@Getter
	private final Dimension2D canvasSize;
	private KeyListener keyListener;
	private JFrame window;
	private JPanel drawablePanel;

	public GamePanel(final Dimension2D canvasSize, final WindowSizeResolver windowSizeResolver) {
		this.canvasSize = canvasSize;
		this.windowSize = windowSizeResolver.dimension();
		this.keyListener = new NullCustomKeyListener();
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
		drawablePanel = new JPanel();
		drawablePanel.setPreferredSize(windowSize);
		drawablePanel.setFocusable(true);
		drawablePanel.requestFocus();
		drawablePanel.addKeyListener(this);

		window = new JFrame();
		window.setTitle("Super Mario Bros Extreme Edition");
		window.setContentPane(drawablePanel);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}

	@Override
	public void draw(final BufferedImage frame) {
		drawablePanel.getGraphics().drawImage(frame, 0, 0, (int) windowSize.getWidth(), (int) windowSize.getHeight(), null);
	}

	@Override
	public void close() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
}
