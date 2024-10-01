package com.fabbroniko.main;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

@Component
public final class SwingGameRenderer implements GameRenderer {

	private final Dimension windowSize;
	private final KeyListener keyListener;
	private JFrame window;
	private JPanel drawablePanel;

	public SwingGameRenderer(final WindowSizeResolver windowSizeResolver,
							 @Qualifier("bridgedKeyListener") final KeyListener keyListener) {

		this.windowSize = windowSizeResolver.dimension();
		this.keyListener = keyListener;
	}

	@Override
	public void init() {
		drawablePanel = new JPanel();
		drawablePanel.setPreferredSize(windowSize);
		drawablePanel.setFocusable(true);
		drawablePanel.requestFocus();
		drawablePanel.addKeyListener(keyListener);

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
