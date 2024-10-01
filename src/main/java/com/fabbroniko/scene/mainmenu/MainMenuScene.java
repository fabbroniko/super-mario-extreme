package com.fabbroniko.scene.mainmenu;

import com.fabbroniko.environment.BiDirectionalState;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.input.UIKeyListener;
import com.fabbroniko.scene.Scene;
import com.fabbroniko.scene.SceneContext;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.factory.SceneContextFactory;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.InitializableDrawable;
import com.fabbroniko.ui.OptionFactory;
import com.fabbroniko.ui.background.BackgroundLoader;
import com.fabbroniko.ui.text.TextFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_UP;

@Component
public final class MainMenuScene implements Scene, UIKeyListener {

	private static final String TITLE = "Super Mario Extreme Edition";
	private static final String START_GAME_OPTION = "Start";
	private static final String SETTINGS_OPTION = "Settings";
	private static final String QUIT_OPTION = "Quit";
	private static final String HINT = "Arrow UP/DOWN to navigate. ENTER to confirm.";

	private static final int TITLE_Y = 120;
	private static final int START_OPTION_Y = 260;
	private static final int SETTINGS_OPTION_Y = 460;
	private static final int QUIT_OPTION_Y = 660;
	private static final int HINT_Y = 920;

	private DrawableResource background;

	private final BiDirectionalState<MainMenuState> states;

	private final SceneContextFactory sceneContextFactory;
	private final SceneManager sceneManager;
	private final TextFactory textFactory;
	private final OptionFactory optionFactory;
	private final BackgroundLoader backgroundLoader;

	private BufferedImage canvas;
	private Graphics2D graphics;
	private Dimension2D canvasDimension;

	public MainMenuScene(final SceneContextFactory sceneContextFactory,
                         final SceneManager sceneManager,
                         final TextFactory textFactory,
						 final OptionFactory optionFactory,
						 final BackgroundLoader backgroundLoader,
						 final StateFactory<MainMenuState> stateStateFactory) {

		this.sceneContextFactory = sceneContextFactory;
		this.sceneManager = sceneManager;
		this.textFactory = textFactory;
        this.optionFactory = optionFactory;
		this.backgroundLoader = backgroundLoader;
		this.states = stateStateFactory.create();
    }

	@Override
	public void init() {
		final InitializableDrawable initializableBackground = backgroundLoader.createStaticBackground("menu");
		initializableBackground.init();
		background = initializableBackground.getDrawableResource();

		final SceneContext sceneContext = sceneContextFactory.create();
		this.canvas = sceneContext.canvas();
		this.graphics = (Graphics2D) canvas.getGraphics();
		this.canvasDimension = sceneContext.canvasDimension();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void update() {
	}

	@Override
	public BufferedImage draw() {
		graphics.drawImage(
				background.image(),
				background.position().getRoundedX(),
				background.position().getRoundedY(),
				canvasDimension.width(),
				canvasDimension.height(),
				null
		);

		final BufferedImage title = textFactory.createHeader(TITLE, Color.GREEN);
		int x = (canvasDimension.width() - title.getWidth()) / 2;
		graphics.drawImage(title, null, x, TITLE_Y);

		// TODO proper use of state pattern here
		final List<MenuOption> menuOptions = states.getCurrent().getOptions();
		printMenuOption(START_GAME_OPTION, START_OPTION_Y, menuOptions.get(0).selected());
		printMenuOption(SETTINGS_OPTION, SETTINGS_OPTION_Y, menuOptions.get(1).selected());
		printMenuOption(QUIT_OPTION, QUIT_OPTION_Y, menuOptions.get(2).selected());

		final BufferedImage hint = textFactory.createSmallParagraph(HINT, Color.WHITE);
		x = (canvasDimension.width() - hint.getWidth()) / 2;
		graphics.drawImage(hint, null, x, HINT_Y);

		return canvas;
	}

	private void printMenuOption(final String text, final int y, final boolean isSelected) {
		final Color color = isSelected ? Color.GREEN : Color.WHITE;

		final BufferedImage startGameOption = optionFactory.getMainMenuOption(text, color);
		int x = (canvasDimension.width() - startGameOption.getWidth()) / 2;
		graphics.drawImage(startGameOption, null, x, y);
	}

	@Override
	public void keyReleased(final KeyEvent event) {
		if(VK_DOWN == event.getKeyCode()) {
			states.next();
		} else if (VK_UP == event.getKeyCode()) {
			states.previous();
		} else if (VK_ENTER == event.getKeyCode()) {
			states.getCurrent().onConfirm();
		} else if (VK_ESCAPE == event.getKeyCode()) {
			sceneManager.quit();
		}
	}

	@Override
	public void close() {
	}
}
