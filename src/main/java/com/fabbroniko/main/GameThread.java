package com.fabbroniko.main;

import com.fabbroniko.GameManager;
import com.fabbroniko.environment.Dimension;
import lombok.SneakyThrows;

import java.awt.Graphics2D;

public class GameThread extends Thread {

    private volatile boolean running = false;
    private final GamePanel gamePanel;
    private final Dimension canvasDimension;
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
        final Dimension canvasDimension = this.canvasDimension;
        final Graphics2D canvas = this.gamePanel.getCanvas();
        running = true;

        // Game Loop
        while (running) {
            gameManager.update();
            gameManager.draw(canvas, canvasDimension);
            gamePanel.repaint();

            Time.sync(gameManager.getSettings().getFpsCap());
        }

        System.exit(0);
    }

    public void exit() {
        running = false;
    }
}
