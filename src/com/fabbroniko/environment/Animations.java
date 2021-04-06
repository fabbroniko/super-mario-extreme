package com.fabbroniko.environment;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
* Represents the available animations.
* @author com.fabbroniko
*/
public enum Animations {

	/**
	* Used when the player is walking.
	*/
	PLAYER_WALK (ObjectType.TYPE_PLAYER , 2, 3, 5, false),


	/**
	* Used when the player is jumping.
	*/
	PLAYER_JUMP (ObjectType.TYPE_PLAYER , 1, 1, 1, true),

	/**
	* Used when the player is still.
	*/
	PLAYER_STILL (ObjectType.TYPE_PLAYER , 0, 1, 1, true),

	
	/**
	* Used for normal blocks.
	*/
	BLOCK_NORMAL (ObjectType.TYPE_BLOCK , 0, 1, 1, true),

	/**
	* Used when a normal block is going to be broken.
	*/
	BLOCK_BREAKING (ObjectType.TYPE_BLOCK , 1, 6, 4, true),

	/**
	* Used when an enemy is walking.
	*/
	ENEMY_WALK (ObjectType.TYPE_ENEMY , 0, 2, 5, false),

	/**
	* Used when an enemy is dying.
	*/
	ENEMY_DEAD (ObjectType.TYPE_ENEMY , 1, 1, 7, true),

	/**
	* Used when an invisible block is visible.
	*/
	INVISIBLEBLOCK_VISIBLE (ObjectType.TYPE_INVISIBLE_BLOCK , 1, 1, 1, true),

	/**
	* Used when an invisible block is invisible.
	*/
	INVISIBLEBLOCK_INVISIBLE (ObjectType.TYPE_INVISIBLE_BLOCK , 0, 1, 1, true),

	/**
	* Used when a falling block is falling.
	*/
	FALLING_BLOCK (ObjectType.TYPE_FALLING_BLOCK , 0, 1, 1, true),

	/**
	* Used for the static castle.
	*/
	CASTLE (ObjectType. TYPE_CASTLE , 0, 1, 1, true);

	private List<BufferedImage> sprites;
	private int frameRepetition;
	private boolean repeat;
	private ObjectType objectType;

	private Animations(final ObjectType spriteFilePath, final int row, final int nFrames, final int frameRepetition, final boolean repeat) {
		this.sprites = new ArrayList<>();
		this.frameRepetition = frameRepetition;
		this.repeat = repeat;
		this.objectType = spriteFilePath;
		generateSprites(spriteFilePath, row, nFrames);
	}

	private void generateSprites(final ObjectType spriteType, final int row, final int nFrames){
		final BufferedImage sprite = spriteType.getSpriteSet();
		final Dimension spriteDimension = spriteType.getDimension();
		final int yPosition = row * spriteDimension.getHeight();
		int xPosition = 0;
		
		for(int i = 0; i < nFrames; i++){
			sprites.add(sprite.getSubimage(xPosition, yPosition,
			spriteDimension.getWidth(), spriteDimension.getHeight()));
			xPosition += spriteDimension.getWidth();
		}
	}
	
	public int getFrameRepetitions() {
		return this.frameRepetition;
	}

	public boolean mustBeRepeated() {
		return this.repeat;
	}
	
	public List<BufferedImage> getSprites() {
		return this.sprites;
	}
	
	public ObjectType getObjectType() {
		return this.objectType;
	}
}	
