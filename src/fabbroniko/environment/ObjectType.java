package fabbroniko.environment;

import java.awt.image.BufferedImage;

import fabbroniko.gameobjects.AbstractGameObject;

/**
* Represents the type of an {@link AbstractGameObject AbstractGameObject}.
* @author fabbroniko
*/
public enum ObjectType {
	/**
	* Player's type.
	*/
	TYPE_PLAYER ("/fabbroniko/Mario/MarioSprites.png", new Dimension(28,26)),
	
	/**
	* Enemy's type.
	*/
	TYPE_ENEMY ("/fabbroniko/Enemy/GhostSprites.png", new Dimension(27,48)),

	/**
	* Block's type.
	*/
	TYPE_BLOCK ("/fabbroniko/Blocks/BlockSprites.png", new Dimension(30,30)),

	/**
	* Invisible Block's type.
	*/
	TYPE_INVISIBLE_BLOCK ("/fabbroniko/Blocks/InvisibleBlockSprites.png", new Dimension(30, 30)),

	/**
	* Falling Block's type.
	*/
	TYPE_FALLING_BLOCK ("/fabbroniko/Blocks/FallingBlockSprites.png", new Dimension(90, 30)),

	/**
	* Castle's type.
	*/
	TYPE_CASTLE ("/fabbroniko/Blocks/Castle.png", new Dimension(170, 175)),

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
		return new Dimension(this.spriteDimension);
	}
}
