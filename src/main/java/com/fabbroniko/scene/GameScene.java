package com.fabbroniko.scene;

import com.fabbroniko.environment.Position;
import com.fabbroniko.gameobjects.Block;
import com.fabbroniko.gameobjects.Castle;
import com.fabbroniko.gameobjects.Enemy;
import com.fabbroniko.gameobjects.FallingBlock;
import com.fabbroniko.gameobjects.InvisibleBlock;
import com.fabbroniko.gamestatemanager.AbstractGenericLevel;
import com.fabbroniko.gamestatemanager.GameManager;

/**
 * First Level.
 * @author com.fabbroniko
 */
public final class GameScene extends AbstractGenericLevel {

	private static final String RES_MAP_FILE = "/Levels/Level1.txt";
	
	private static final Position BLOCK1_POSITION = new Position(60, 250);
	private static final Position BLOCK2_POSITION = new Position(90, 250);
	private static final Position ENEMY1_POSITION = new Position(1315, 270);
	private static final Position ENEMY2_POSITION = new Position(2000, 250);
	private static final Position ENEMY3_POSITION = new Position(2350, 250);
	private static final Position FALLING1_POSITION = new Position(270, 270);
	private static final Position FALLING2_POSITION = new Position(390, 190);
	private static final Position FALLING3_POSITION = new Position(480, 150);
	private static final Position FALLING4_POSITION = new Position(610, 300);
	private static final Position FALLING5_POSITION = new Position(700, 260);
	private static final Position FALLING6_POSITION = new Position(1470, 330);
	private static final Position FALLING7_POSITION = new Position(1560, 330);
	private static final Position INVISIBLE1_POSITION = new Position(330, 230);
	private static final Position INVISIBLE2_POSITION = new Position(760, 210);
	private static final Position INVISIBLE3_POSITION = new Position(990, 250);
	private static final Position INVISIBLE4_POSITION = new Position(1020, 170);
	private static final Position INVISIBLE5_POSITION = new Position(1170, 140);
	private static final Position INVISIBLE6_POSITION = new Position(1770, 170);
	private static final Position INVISIBLE7_POSITION = new Position(1740, 240);
	private static final Position INVISIBLE8_POSITION = new Position(1770, 240);
	private static final Position INVISIBLE9_POSITION = new Position(1800, 240);
	private static final Position INVISIBLE10_POSITION = new Position(1830, 240);
	private static final Position CASTLE_POSITION = new Position(2750, 170);
	
	private static final int POSITION_OFFSET = 10;
	
	public GameScene(final GameManager gameManager) {
		super(gameManager, RES_MAP_FILE);
	}

	@Override
	public void init() {
		super.init();

		this.addNewObject(Block.class, BLOCK1_POSITION);
		this.addNewObject(Block.class, BLOCK2_POSITION);
		this.addNewObject(Enemy.class, ENEMY1_POSITION);
		this.addNewObject(Enemy.class, ENEMY2_POSITION);
		this.addNewObject(Enemy.class, ENEMY3_POSITION);
		this.addNewObject(FallingBlock.class, FALLING1_POSITION);
		this.addNewObject(FallingBlock.class, FALLING2_POSITION);
		this.addNewObject(FallingBlock.class, FALLING3_POSITION);
		this.addNewObject(FallingBlock.class, FALLING4_POSITION);
		this.addNewObject(FallingBlock.class, FALLING5_POSITION);
		this.addNewObject(FallingBlock.class, FALLING6_POSITION);
		this.addNewObject(FallingBlock.class, FALLING7_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE1_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE2_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE3_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE4_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE5_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE6_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE7_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE8_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE9_POSITION);
		this.addNewObject(InvisibleBlock.class, INVISIBLE10_POSITION);
		this.addNewObject(Castle.class, CASTLE_POSITION);
	}
	
	@Override
	protected Position getPreferredStartPosition() {
		return new Position(tileMap.getTileSize().getWidth() + POSITION_OFFSET, tileMap.getTileSize().getHeight() + POSITION_OFFSET);
	}
	
	@Override
	public void levelFinished() {
		gameManager.openScene(WinScene.class);
	}
}
