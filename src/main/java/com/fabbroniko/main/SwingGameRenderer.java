package com.fabbroniko.main;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Qualifier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

@Component
public final class SwingGameRenderer implements GameRenderer {

	private final Dimension windowSize;
	private final Dimension effectiveSize;
	private final KeyListener keyListener;
	private JFrame window;
	private JPanel drawablePanel;
	private BufferedImage framedCanvas;
	private Graphics2D framedCanvasGraphics;
	private int centeredX;
	private int centeredY;

	public SwingGameRenderer(@Qualifier("screenSizeResolver") final WindowSizeResolver screenSizeResolver,
							 @Qualifier("effectiveSizeResolver") final WindowSizeResolver effectiveSizeResolver,
							 @Qualifier("bridgedKeyListener") final KeyListener keyListener) {

		this.windowSize = screenSizeResolver.dimension();
		this.effectiveSize = effectiveSizeResolver.dimension();
		this.keyListener = keyListener;
	}

	@Override
	public void init() {
		drawablePanel = new JPanel();
		drawablePanel.setPreferredSize(windowSize);
		drawablePanel.setFocusable(true);
		drawablePanel.requestFocus();
		drawablePanel.addKeyListener(keyListener);

		framedCanvas = new BufferedImage(windowSize.width, windowSize.height, BufferedImage.TYPE_INT_ARGB);
		framedCanvasGraphics = framedCanvas.createGraphics();
		framedCanvasGraphics.setColor(Color.BLACK);
		framedCanvasGraphics.fillRect(0, 0, framedCanvas.getWidth(), framedCanvas.getHeight());
		centeredX = (windowSize.width - effectiveSize.width) / 2;
		centeredY = (windowSize.height - effectiveSize.height) / 2;

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
		framedCanvasGraphics.drawImage(frame, centeredX, centeredY, (int) effectiveSize.getWidth(), (int) effectiveSize.getHeight(), null);
		drawablePanel.getGraphics().drawImage(framedCanvas, 0, 0, null);
	}

	@Override
	public void close() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
}
