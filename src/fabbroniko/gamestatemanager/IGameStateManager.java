package fabbroniko.gamestatemanager;

import fabbroniko.scene.LostScene;
import fabbroniko.scene.GameScene;
import fabbroniko.scene.MainMenuScene;
import fabbroniko.scene.SettingsMenuScene;
import fabbroniko.scene.WinScene;
import fabbroniko.main.Drawable;

public interface IGameStateManager extends Drawable{

	/**
	* Change the currente state to the given one.
	* @param newState State to be displayed.
	*/
	void setState(final State newState);

	/**
	* Represents a state of the game.
	*/
	public enum State {
		MENU_STATE (MainMenuScene.getInstance()),
		SETTINGS_STATE (SettingsMenuScene.getInstance()),
		LEVEL1_STATE (GameScene.getInstance()),
		WIN_STATE (WinScene.getInstance()),
		DEATH_STATE (LostScene.getInstance()),
		NO_STATE (null);

		private final AbstractGameState gameState;

		private State(final AbstractGameState gameState) {
			this.gameState = gameState;
		}


		public AbstractGameState getGameState() {
			return this.gameState;
		}
	}
}
