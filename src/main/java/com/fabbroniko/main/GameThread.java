package com.fabbroniko.main;

import com.fabbroniko.environment.SettingsProvider;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameThread extends Thread {

    private volatile boolean running = false;
    private final GamePanel gamePanel;
    private final GameManager gameManager;
    private final SettingsProvider settingsProvider;

    public GameThread(final GamePanel gamePanel, final SettingsProvider settingsProvider, final GameManager gameManager) {
        super();

        this.settingsProvider = settingsProvider;
        this.gameManager = gameManager;
        this.gamePanel = gamePanel;
    }

    @SneakyThrows
    @Override
    public void run() {
        final Graphics2D canvas = this.gamePanel.getCanvas();
        running = true;

        // Game Loop
        while (running) {
            gameManager.update();

            final BufferedImage frame = gameManager.draw();
            if(frame != null) {
                canvas.drawImage(gameManager.draw(), null, 0, 0);

                gamePanel.repaint();
            }

            Time.sync(settingsProvider.getSettings().getFpsCap());
        }

        System.exit(0);
    }

    public void exit() {
        running = false;
    }
}
