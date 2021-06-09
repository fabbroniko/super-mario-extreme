package com.fabbroniko.main;

import com.fabbroniko.environment.Vector2D;
import lombok.SneakyThrows;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GameThread extends Thread {

    private volatile boolean running = false;
    private final GamePanel gamePanel;
    private final Vector2D canvasDimension;
    private final GameManager gameManager;

    public GameThread(final GamePanel gamePanel, final GameManager gameManager) {
        super();

        this.gameManager = gameManager;
        this.canvasDimension = gameManager.getCanvasSize();
        this.gamePanel = gamePanel;
    }

    @SneakyThrows
    @Override
    public void run() {
        final Vector2D canvasDimension = this.canvasDimension;
        final Graphics2D canvas = this.gamePanel.getCanvas();
        running = true;

        // Game Loop
        while (running) {
            final BufferedImage bufferedImage = new BufferedImage(canvasDimension.getRoundedX(), canvasDimension.getRoundedY(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D bufferedCanvas = bufferedImage.createGraphics();

            gameManager.update();
            gameManager.draw(bufferedCanvas, canvasDimension);

            canvas.drawImage(bufferedImage, null, 0, 0);

            gamePanel.repaint();

            Time.sync(gameManager.getSettings().getFpsCap());
        }

        System.exit(0);
    }

    public void exit() {
        running = false;
    }
}
