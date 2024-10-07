package com.fabbroniko.gameobjects;

import com.fabbroniko.audio.EffectPlayerProvider;
import com.fabbroniko.collision.CollisionDirection;
import com.fabbroniko.collision.CollisionManager;
import com.fabbroniko.environment.BoundingBox;
import com.fabbroniko.environment.Dimension2D;
import com.fabbroniko.environment.ImmutableDimension2D;
import com.fabbroniko.environment.ImmutablePosition;
import com.fabbroniko.environment.Position;
import com.fabbroniko.environment.Vector2D;
import com.fabbroniko.input.TypedLessKeyListener;
import com.fabbroniko.main.CustomKeyListener;
import com.fabbroniko.main.Time;
import com.fabbroniko.map.TileMap;
import com.fabbroniko.resource.ImageLoader;
import com.fabbroniko.scene.LostScene;
import com.fabbroniko.scene.SceneManager;
import com.fabbroniko.scene.mainmenu.MainMenuScene;
import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Prototype;
import com.fabbroniko.sdi.annotation.Qualifier;
import com.fabbroniko.settings.SettingsProvider;
import com.fabbroniko.ui.DrawableResource;
import com.fabbroniko.ui.DrawableResourceImpl;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_ESCAPE;

@Prototype
@Component
public class Tester implements TypedLessKeyListener, GameObject {

	private static final Dimension2D spriteDimension = new ImmutableDimension2D(112, 104);
	private static final String spritePath = "/sprites/test.png";

	private final Dimension2D baseWindowSize;

	private final SettingsProvider settingsProvider;
	protected BoundingBox boundingBox;

	protected Vector2D mapPosition = new Vector2D();
	protected Animation currentAnimation;
	private final TileMap tileMap;
	private final CollisionManager collisionManager;
	private final SceneManager sceneManager;
	private final EffectPlayerProvider effectPlayerProvider;

	protected boolean up;
	protected boolean down;
	protected boolean left;
	protected boolean right;

	protected int jumpSpeed = -1000;
	protected int gravitySpeed = 600;
	protected int walkingSpeed = 600;

	public Tester(final TileMap tileMap,
				  @Qualifier("canvasSize") final Dimension2D baseWindowSize,
				  final SettingsProvider settingsProvider,
				  final CollisionManager collisionManager,
				  @Qualifier("cachedImageLoader") final ImageLoader imageLoader,
				  final SceneManager sceneManager,
				  final EffectPlayerProvider effectPlayerProvider,
				  final CustomKeyListener customKeyListener) {

		this.tileMap = tileMap;
		this.collisionManager = collisionManager;
		this.sceneManager = sceneManager;
		this.effectPlayerProvider = effectPlayerProvider;

		this.settingsProvider = settingsProvider;

		down = true;
		this.baseWindowSize = baseWindowSize;

		this.currentAnimation = Animation.builder()
			.spriteSet(imageLoader.findSpritesByName(spritePath))
			.spriteDimension(spriteDimension)
			.row(1)
			.nFrames(1)
			.name("tester")
			.mirror()
			.build();

		customKeyListener.setKeyListener(this);
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == settingsProvider.getSettings().getLeftMovementKeyCode()) {
			left = true;
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getRightMovementKeyCode()) {
			right = true;
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getJumpKeyCode()) {
			up = true;
			effectPlayerProvider.getEffectPlayer().play("jump");
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		if (VK_ESCAPE == e.getKeyCode()) {
			sceneManager.openScene(MainMenuScene.class);
		}

		if (e.getKeyCode() == settingsProvider.getSettings().getLeftMovementKeyCode()) {
			left = false;
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getRightMovementKeyCode()) {
			right = false;
		}
		if (e.getKeyCode() == settingsProvider.getSettings().getJumpKeyCode()) {
			up = false;
		}
	}

	@Override
	public void update() {
		double xOffset = 0;
		double yOffset = 0;

		mapPosition.setPosition(tileMap.getPosition());

		if (up) {
			yOffset += (jumpSpeed * Time.deltaTime());
		}

		yOffset += down && !up ? (gravitySpeed * Time.deltaTime()) : 0;
		xOffset += left ? (-walkingSpeed * Time.deltaTime()) : 0;
		xOffset += right ? (walkingSpeed * Time.deltaTime()) : 0;

		if (xOffset != 0 || yOffset != 0) {
			final Position offset = collisionManager.calculateMovement(this, new Vector2D(xOffset, yOffset));
			boundingBox.position().setPosition(boundingBox.position().getX() + offset.getX(), boundingBox.position().getY() + offset.getY());
		}

		if(tileMap.isOutsideBounds(boundingBox)) {
			notifyDeath();
		}

		tileMap.setPosition(boundingBox.position().getRoundedX() - (baseWindowSize.width() / 2), boundingBox.position().getRoundedY() - (baseWindowSize.height() / 2));
	}

	@Override
	public DrawableResource getDrawableResource() {
		final Position position = new ImmutablePosition(boundingBox.position().getRoundedX() - mapPosition.getX(), boundingBox.position().getRoundedY() - mapPosition.getY());
		return new DrawableResourceImpl(currentAnimation.getImage(), position);
	}

	@Override
	public void setInitialPosition(final Position position) {
		this.boundingBox = new BoundingBox(position, spriteDimension);
	}

	@Override
	public void notifyDeath() {
		sceneManager.openScene(LostScene.class);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public void handleMapCollisions(final CollisionDirection direction) {
	}

	@Override
	public void handleObjectCollisions(final CollisionDirection direction, final GameObject gameObject) {
	}

	@Override
	public boolean isDead() {
		return false;
	}
}
