package fabbroniko.gameobjects;

import java.awt.event.KeyEvent;

import fabbroniko.environment.Animation;
import fabbroniko.environment.Animations;
import fabbroniko.environment.AudioManager;
import fabbroniko.environment.CollisionDirection;
import fabbroniko.environment.Dimension;
import fabbroniko.environment.ObjectType;
import fabbroniko.environment.TileMap;
import fabbroniko.gamestatemanager.GameStateManager;
import fabbroniko.gamestatemanager.AbstractGenericLevel;
import fabbroniko.gamestatemanager.IGameStateManager.State;
import fabbroniko.gamestatemanager.gamestates.DeathState;
import fabbroniko.gamestatemanager.gamestates.SettingsState;

/**
 * Represents the player's character.
 * @author fabbroniko
 */
public class Player extends AbstractGameObject {
	
	private boolean animationJump;
	private boolean animationMove;
	private final AbstractGenericLevel currentLevel;
	
	private Dimension baseWindowSize;
	
	private Animation animationWalk = new Animation(Animations.PLAYER_WALK);
	private Animation animationStill = new Animation(Animations.PLAYER_STILL);
	private Animation animationJumpA = new Animation(Animations.PLAYER_JUMP);
	
	/**
	 * Constructs the player instance.
	 * @param tileMap Reference of the {@link TileMap TileMap} on which it should be placed.
	 * @param level Reference of the {@link AbstractGenericLevel AbstractGenericLevel} on which it should be placed.
	 */
	public Player(final TileMap tileMap, final AbstractGenericLevel level, final Integer objectID) {
		super(tileMap, level, Animations.PLAYER_JUMP, objectID);
		falling = true;
		animationJump = true;
		facingRight = true;
		this.objectType = ObjectType.TYPE_PLAYER;
		this.currentLevel = level;
		this.baseWindowSize = GameStateManager.getInstance().getBaseWindowSize();
	}
	
	@Override
	public void update() {
		super.update();
		tileMap.setPosition(this.getObjectPosition().getX() - (int) (baseWindowSize.getWidth() / 2), this.getObjectPosition().getY() - (int) (baseWindowSize.getHeight() / 2));
		if (death) {
			DeathState.getInstance().incDeath();
			GameStateManager.getInstance().setState(State.DEATH_STATE);
		}
		if (animationJump) {
			this.currentAnimation = animationJumpA;
		} else if (animationMove) {
			this.currentAnimation = animationWalk;
		} else {
			this.currentAnimation = animationStill;
		}
	}
	
	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
		super.handleMapCollisions(direction);
		
		if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
			animationJump = false;
		}
	}
	 
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		if (!obj.getObjectType().equals(ObjectType.TYPE_INVISIBLE_BLOCK) || obj.getObjectType().equals(ObjectType.TYPE_INVISIBLE_BLOCK) && obj.currentAnimation.getAnimation().equals(Animations.INVISIBLEBLOCK_VISIBLE) || obj.objectType.equals(ObjectType.TYPE_INVISIBLE_BLOCK) && direction.equals(CollisionDirection.TOP_COLLISION)) { 
			super.handleObjectCollisions(direction, obj);
		}
		
		if (obj.getObjectType().equals(ObjectType.TYPE_ENEMY)) {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				jumping = true;
			} else {
				death = true;
			}
		} else if (obj.getObjectType().equals(ObjectType.TYPE_CASTLE)) {
			this.currentLevel.levelFinished();
		} else {
			if (direction.equals(CollisionDirection.BOTTOM_COLLISION)) {
				animationJump = false;
				groundHit = true;
			}
		}
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == SettingsState.getInstance().getLeftKeyCode()) {
			left = true;
			animationMove = true;
			facingRight = false;
		}
		if (e.getKeyCode() == SettingsState.getInstance().getRightKey()) {
			right = true;
			animationMove = true;
			facingRight = true;
		}
		if (e.getKeyCode() == SettingsState.getInstance().getJumpKey() && !jumping && groundHit) {
			jumping = true;
			groundHit = false;
			currentJump = 0;
			animationJump = true;
			AudioManager.getInstance().playSound(fabbroniko.resources.Sound.getSoundFromName("JumpSound"));
		}
	}
 
	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.getKeyCode() == SettingsState.getInstance().getLeftKeyCode()) {
			left = false; 
			animationMove = false;
		}
		if (e.getKeyCode() == SettingsState.getInstance().getRightKey()) {
			right = false; 	
			animationMove = false;
		}
		if (e.getKeyCode() == SettingsState.getInstance().getJumpKey()) {
			jumping = false;
		}
	}
}
