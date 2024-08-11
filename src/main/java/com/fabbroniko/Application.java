package com.fabbroniko;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.AudioManagerImpl;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.SceneContextFactory;
import com.fabbroniko.environment.SceneContextFactoryImpl;
import com.fabbroniko.main.GameManager;
import com.fabbroniko.main.GamePanel;
import com.fabbroniko.main.GameWindow;
import com.fabbroniko.main.SettingsProvider;
import com.fabbroniko.main.SettingsProviderImpl;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.scene.SceneFactory;
import com.fabbroniko.scene.SceneFactoryImpl;

import java.awt.*;

public class Application {

    private static final int CANVAS_WIDTH = 1280;
    private static final int CANVAS_HEIGHT = 960;

    public static void main(final String[] args) {
        final ResourceManager resourceManager = new ResourceManager();
        final SettingsProvider settingsProvider = new SettingsProviderImpl(resourceManager);
        final SceneContextFactory sceneContextFactory = new SceneContextFactoryImpl(CANVAS_WIDTH, CANVAS_HEIGHT);
        final AudioManager audioManager = new AudioManagerImpl(settingsProvider, resourceManager);
        final SceneFactory sceneFactory = new SceneFactoryImpl(sceneContextFactory, settingsProvider, audioManager, resourceManager);
        final Dimension2D canvasDimension = new Dimension2D(CANVAS_WIDTH, CANVAS_HEIGHT);
        final java.awt.Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension2D windowDimension = new Dimension2D((int) screenDimensions.getWidth(), (int) screenDimensions.getHeight());;
        final GamePanel gamePanel = new GamePanel(canvasDimension, windowDimension);
        new GameWindow(gamePanel);
        final GameManager gameManager = new GameManager(gamePanel, resourceManager, settingsProvider, sceneFactory);

        gameManager.start();
    }
}
