package fabbroniko.gamestatemanager;

import fabbroniko.gamestatemanager.gamestates.DeathState;
import fabbroniko.gamestatemanager.gamestates.Level1State;
import fabbroniko.gamestatemanager.gamestates.MenuState;
import fabbroniko.gamestatemanager.gamestates.SettingsState;
import fabbroniko.gamestatemanager.gamestates.WinState;
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
		MENU_STATE (MenuState.getInstance()),
		SETTINGS_STATE (SettingsState.getInstance()),
		LEVEL1_STATE (Level1State.getInstance()),
		WIN_STATE (WinState.getInstance()),
		DEATH_STATE (DeathState.getInstance()),
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
