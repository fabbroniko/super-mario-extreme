package com.fabbroniko.gameobjects;

import com.fabbroniko.environment.Vector2D;
import lombok.extern.log4j.Log4j2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class Animation {

	private static final int START_INDEX = 0;

	private List<BufferedImage> frames;
	private List<BufferedImage> mirroredFrames;

	private long frameDuration;
	private AnimationListener animationListener;
	private String name;

	private long frameStartingTimestamp;
	private int currentImageIndex;

	public static Builder builder() {
		return new Animation.Builder();
	}

	private Animation() {
		reset();
	}

	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return getImage(frames);
	}

	public BufferedImage getMirroredImage() {
		if(mirroredFrames == null)
			return getImage(frames);
		else
			return getImage(mirroredFrames);
	}

	private BufferedImage getImage(final List<BufferedImage> pFrames) {
		final long currentMillis = System.currentTimeMillis();
		if ((currentMillis - frameStartingTimestamp) >= frameDuration) {
			frameStartingTimestamp = currentMillis;
			currentImageIndex++;

			if(currentImageIndex >= frames.size()) {
				currentImageIndex = START_INDEX ;
				if (animationListener != null) {
					animationListener.animationFinished();
				}
			}
		}

		return pFrames.get(currentImageIndex);
	}

	public void reset() {
		currentImageIndex = 0;
		frameStartingTimestamp = System.currentTimeMillis();
	}

	public final static class Builder {

		private static final long STATIC_ANIMATION = Long.MAX_VALUE;

		private BufferedImage spriteSet;
		private String name;
		private AnimationListener animationListener;
		private long frameDuration = STATIC_ANIMATION;
		private int nFrames = 1;
		private int row = 0;
		private Vector2D spriteDimension = new Vector2D();
		private boolean mirror;

		public Builder() {}

		public Builder spriteSet(final BufferedImage spriteSet) {
			this.spriteSet = spriteSet;
			return this;
		}

		public Builder name(final String name) {
			this.name = name;
			return this;
		}

		public Builder animationListener(final AnimationListener animationListener) {
			this.animationListener = animationListener;
			return this;
		}

		public Builder frameDuration(final long frameDuration) {
			this.frameDuration = frameDuration;
			return this;
		}

		public Builder nFrames(final int nFrames) {
			this.nFrames = nFrames;
			return this;
		}

		public Builder row(final int row) {
			this.row = row;
			return this;
		}

		public Builder mirror() {
			mirror = true;
			return this;
		}

		public Builder spriteDimension(final Vector2D dimension) {
			this.spriteDimension = dimension;
			return this;
		}

		public Animation build() {
			if(name == null) {
				log.error("animation_builder,build,invalid_state,name_not_set");
				throw new IllegalArgumentException("Can't build animation without a unique name identifier.");
			}

			if(spriteSet == null) {
				log.error("animation_builder,build,invalid_state,sprite_set_not_valid");
				throw new IllegalArgumentException("Can't build animation without the sprite set for the game object.");
			}

			final Animation animation = new Animation();
			animation.name = this.name;
			animation.animationListener = this.animationListener;
			animation.frameDuration = this.frameDuration;
			animation.frames = generateSprites();

			if(mirror)
				animation.mirroredFrames = generateMirroredSprites(animation.frames);

			return animation;
		}

		private List<BufferedImage> generateSprites(){
			final int yPosition = row * spriteDimension.getRoundedY();

			int xPosition = 0;
			final List<BufferedImage> sprites = new ArrayList<>();
			for(int i = 0; i < nFrames; i++){
				sprites.add(spriteSet.getSubimage(xPosition, yPosition, spriteDimension.getRoundedX(), spriteDimension.getRoundedY()));
				xPosition += spriteDimension.getRoundedX();
			}

			return sprites;
		}

		private List<BufferedImage> generateMirroredSprites(final List<BufferedImage> frames) {
			final List<BufferedImage> mirroredFrames = new ArrayList<>();

			frames.forEach(animation -> {
				final BufferedImage mirroredFrame = new BufferedImage(spriteDimension.getRoundedX(), spriteDimension.getRoundedY(), BufferedImage.TYPE_INT_ARGB);
				final Graphics2D g = mirroredFrame.createGraphics();
				g.drawImage(animation, spriteDimension.getRoundedX(), 0, -spriteDimension.getRoundedX(), spriteDimension.getRoundedY(), null);

				mirroredFrames.add(mirroredFrame);
			});

			return mirroredFrames;
		}
	}
}