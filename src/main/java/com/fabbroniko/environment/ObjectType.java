package com.fabbroniko.environment;

import java.awt.image.BufferedImage;

import com.fabbroniko.gameobjects.AbstractGameObject;

/**
* Represents the type of an {@link AbstractGameObject AbstractGameObject}.
* @author com.fabbroniko
*/
public enum ObjectType {
	/**
	* Player's type.
	*/
	TYPE_PLAYER ("/Mario/MarioSprites.png", new Dimension(28,26)),
	
	/**
	* Enemy's type.
	*/
	TYPE_ENEMY ("/Enemy/GhostSprites.png", new Dimension(27,48)),

	/**
	* Block's type.
	*/
	TYPE_BLOCK ("/Blocks/BlockSprites.png", new Dimension(30,30)),

	/**
	* Invisible Block's type.
	*/
	TYPE_INVISIBLE_BLOCK ("/Blocks/InvisibleBlockSprites.png", new Dimension(30, 30)),

	/**
	* Falling Block's type.
	*/
	TYPE_FALLING_BLOCK ("/Blocks/FallingBlockSprites.png", new Dimension(90, 30)),

	/**
	* Castle's type.
	*/
	TYPE_CASTLE ("/Blocks/Castle.png", new Dimension(170, 175)),

	/**
	* Unknown Type.
	*/
	TYPE_NONE (null, null);

	private BufferedImage spriteSet;
	private Dimension spriteDimension;

	private ObjectType(final String spriteFilePath, final Dimension spriteDimension) {
		if(spriteDimension != null && spriteFilePath != null){
			this.spriteSet = Service.getImageFromName(spriteFilePath);
			this.spriteDimension = spriteDimension;
		}
	}

	public BufferedImage getSpriteSet() {
		return this.spriteSet;
	}

	public Dimension getDimension() {
		return spriteDimension;
	}
}
