package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.GameScene;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameObject implements GameObject {

	protected BoundingBox boundingBox;

	protected Vector2D mapPosition;
	protected Animation currentAnimation;
	protected List<Animation> registeredAnimations;
	protected final TileMap tileMap;
	protected final GameScene gameScene;
	protected final ImageLoader imageLoader;
	protected final EffectPlayerProvider effectPlayerProvider;

	protected boolean jumping;
	protected boolean falling;
	protected boolean left;
	protected boolean right;
	protected boolean facingRight;
	protected boolean groundHit;
	protected int currentJump;
	protected boolean death;
	protected int jumpSpeed = -1000;
	protected int gravitySpeed = 600;
	protected int walkingSpeed = 600;
	protected int maxJump = 400;

	protected Vector2D offset;

	protected AbstractGameObject(final TileMap tileMap,
								 final GameScene gameScene,
								 final ImageLoader imageLoader,
								 final EffectPlayerProvider effectPlayerProvider,
								 final Vector2D position,
								 final Dimension2D dimension) {
		this.tileMap = tileMap;
		this.gameScene = gameScene;
		this.imageLoader = imageLoader;
		this.effectPlayerProvider = effectPlayerProvider;
		this.death = false;
		
		this.boundingBox = new BoundingBox(position, dimension);
		this.registeredAnimations = new ArrayList<>();

		offset = new Vector2D();
		mapPosition = new Vector2D();
	}
}