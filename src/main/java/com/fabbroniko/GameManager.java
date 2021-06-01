package com.fabbroniko;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import com.fabbroniko.environment.AudioManager;
import com.fabbroniko.environment.Dimension;
import com.fabbroniko.main.Drawable;
import com.fabbroniko.main.GamePanel;
import com.fabbroniko.main.GameWindow;
import com.fabbroniko.main.Time;
import com.fabbroniko.resource.ResourceManager;
import com.fabbroniko.resource.domain.Level;
import com.fabbroniko.scene.AbstractScene;
import com.fabbroniko.scene.GameScene;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.MainMenuScene;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

public final class GameManager implements Drawable {

	private static final GameManager instance = new GameManager();

	private final Object synchronize;
	private final ResourceManager resourceManager;
	private final AudioManager audioManager;

	private final Settings settings;

	private GamePanel gamePanel;
	private GameThread gameThread;
	private AbstractScene currentState;
	private int deathCount;

	private GameManager() {
		this.synchronize = new Object();
		this.resourceManager = new ResourceManager();
		this.audioManager = new AudioManager(this, resourceManager);
		this.settings = resourceManager.loadSettings();
		this.deathCount = 0;

		this.resourceManager.preload();
	}

	public static GameManager getInstance() {
		return instance;
	}
	
	/**
	 * Sets the specified state that has to be displayed on the screen.
	 */
	@SneakyThrows
	public void openScene(final Class<? extends AbstractScene> newSceneClass) {
		AbstractScene newScene;

		if(GameScene.class.equals(newSceneClass)) {
			final Level defaultLevel = new XmlMapper().readValue(getClass().getResource("/levels/lvl1.xml"), Level.class);
			newScene = newSceneClass.getConstructor(GameManager.class, Level.class).newInstance(this, defaultLevel);
		} else {
			newScene = newSceneClass.getConstructor(GameManager.class).newInstance(this);
		}

		if(newScene instanceof LostScene) {
			deathCount++;
		}

		synchronized (synchronize) {
			if(currentState != null) {
				currentState.detachScene();
			}

			this.currentState = newScene;
			this.currentState.init();
		}
	}
	
	/**	Updates the image that should be displayed.
	 * 	@see com.fabbroniko.main.Drawable#update()
	 */
	public void update() {
		synchronized (synchronize) {
			if(currentState != null) this.currentState.update();
		}
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public ResourceManager getResourceManager() { return resourceManager; }

	public Settings getSettings() {
		return settings;
	}

	public void saveSettings() {
		resourceManager.saveSettings(settings);
	}

	public void draw(final Graphics2D g, final Dimension gDimension) {
		synchronized (synchronize) {
			if(currentState != null) this.currentState.draw(g, gDimension);
		}
	}
	
	public void exit() {
		gameThread.exit();
	}

	public void addKeyListener(final KeyListener keyListener) {
		gamePanel.addKeyListener(keyListener);
	}

	public void removeKeyListener(final KeyListener keyDependent) {
		gamePanel.removeKeyListener(keyDependent);
	}

	public int getDeathCount() {
		return deathCount;
	}

	public void start() {
		gamePanel = new GameWindow().getView();

		gameThread = new GameThread();
		gameThread.start();

		openScene(MainMenuScene.class);
	}

	public Dimension getCanvasSize() {
		return gamePanel.getCanvasSize();
	}

	private class GameThread extends Thread {

		private volatile boolean running = false;
		private long variableYieldTime, lastTime;

		@SneakyThrows
		@Override
		public void run() {
			final Dimension canvasDimension = getCanvasSize();
			final Graphics2D canvas = gamePanel.getCanvas();
			running = true;

			// Game Loop
			while (running) {
				Time.tick();

				update();
				draw(canvas, canvasDimension);
				gamePanel.repaint();

				sync(settings.getFpsCap());
			}

			System.exit(0);
		}

		public void exit() {
			running = false;
		}

		/**
		 * A modified version from the LWJGL Library
		 *
		 * An accurate sync method that adapts automatically
		 * to the system it runs on to provide reliable results.
		 *
		 * @param fps The desired frame rate, in frames per second
		 * @author kappa (On the LWJGL Forums)
		 */
		private void sync(int fps) {
			if (fps <= 0) return;

			long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
			// yieldTime + remainder micro & nano seconds if smaller than sleepTime
			long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
			long overSleep = 0; // time the sync goes over by

			try {
				while (true) {
					long t = System.nanoTime() - lastTime;

					if (t < sleepTime - yieldTime) {
						Thread.sleep(1);
					}else if (t < sleepTime) {
						// burn the last few CPU cycles to ensure accuracy
						Thread.yield();
					}else {
						overSleep = t - sleepTime;
						break; // exit while loop
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);

				// auto tune the time sync should yield
				if (overSleep > variableYieldTime) {
					// increase by 200 microseconds (1/5 a ms)
					variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
				}
				else if (overSleep < variableYieldTime - 200*1000) {
					// decrease by 2 microseconds
					variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
				}
			}
		}
	}
}
