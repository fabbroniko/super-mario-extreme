package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.*;
import com.fabbroniko.scene.GameScene;
import lombok.extern.log4j.Log4j2;

/**
 * This class describes the behaviour of the mob "ghost".
 *
 * This mob simply walks in one direction until it hits any solid tile of the map, making it invert direction.
 * It can only be killed by the player if hit from the top or if it falls outside the boundaries of the map.
 *
 * The Player is killed if it hits the ghost in any other direction.
 */
@Log4j2
public class Ghost extends AbstractGameObject implements AnimationListener {

	private static final Vector2D spriteDimension = new Vector2D(108, 192);
	private static final String spritePath = "/sprites/ghost.png";
	private static final String ENEMY_WALK_ANIMATION_NAME = "ENEMY_WALK";
	private static final String ENEMY_DEAD_ANIMATION_NAME = "ENEMY_DEAD";

	// Reference to the death animation when the player hits the ghost from the top. Once the animation is completed the GO is killed.
	private final Animation deadAnimation;

	public Ghost(final TileMap tileMap, final GameScene gameScene, final Vector2D position) {
		super(tileMap, gameScene, position, spriteDimension);

		properties.add(GameObjectProperty.FALLING);
		properties.add(GameObjectProperty.MOVEMENT_LEFT);
		walkingSpeed = 300;

		deadAnimation = Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(1)
				.nFrames(1)
				.frameDuration(200)
				.animationListener(this)
				.name(ENEMY_DEAD_ANIMATION_NAME)
				.mirror()
				.build();

		setAnimation(Animation.builder()
				.spriteSet(gameScene.getResourceManager().loadImageFromDisk(spritePath))
				.spriteDimension(spriteDimension)
				.row(0)
				.nFrames(2)
				.frameDuration(100)
				.name(ENEMY_WALK_ANIMATION_NAME)
				.mirror()
				.build());

		log.info("enemy,hash_code_{},position_{}", hashCode(), position);
	}
	
	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
		super.handleMapCollisions(direction);

		log.trace("enemy,hash_code_{},map_collision_direction_{}", hashCode(), direction);
		if (!CollisionDirection.LEFT_COLLISION.equals(direction) && !CollisionDirection.RIGHT_COLLISION.equals(direction)) {
			return;
		}

		if (properties.contains(GameObjectProperty.MOVEMENT_LEFT)) {
			log.trace("enemy,hash_code_{},map_collision_direction_{},swapping_movement_direction_to_right", hashCode(), direction);

			properties.remove(GameObjectProperty.MOVEMENT_LEFT);
			properties.add(GameObjectProperty.MOVEMENT_RIGHT);
			properties.add(GameObjectProperty.FACING_RIGHT);
		} else {
			log.trace("enemy,hash_code_{},map_collision_direction_{},swapping_movement_direction_to_left", hashCode(), direction);

			properties.remove(GameObjectProperty.MOVEMENT_RIGHT);
			properties.remove(GameObjectProperty.FACING_RIGHT);
			properties.add(GameObjectProperty.MOVEMENT_LEFT);
		}
	}
	
	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final AbstractGameObject obj) {
		super.handleObjectCollisions(direction, obj);

		log.trace("enemy,hash_code_{},direction_{},game_object_{},handling_collision_with_game_object", hashCode(), direction, obj);
		if (direction.equals(CollisionDirection.TOP_COLLISION) && obj instanceof Player && !currentAnimation.getName().equals(ENEMY_DEAD_ANIMATION_NAME)) {
			this.setAnimation(deadAnimation);
			this.gameScene.getAudioManager().playEffect("hit");

			log.info("enemy,hash_code_{},stepped_on_by_player", hashCode());
		}
	}

	@Override
	public void animationFinished() {
		log.info("enemy,hash_code_{},killed", hashCode());

		notifyDeath();
	}
}
