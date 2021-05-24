package com.fabbroniko.environment;

import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class Animation {

	private static final int START_INDEX = 0;

	private List<BufferedImage> frames;
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

		return frames.get(currentImageIndex);
	}

	public void reset() {
		currentImageIndex = 0;
		frameStartingTimestamp = System.currentTimeMillis();
	}

	@Override
	public boolean equals(final Object o) {
		if(!(o instanceof Animation)){
			return false;
		}

		return ((Animation)o).name.equals(this.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public final static class Builder {

		private static final long STATIC_ANIMATION = Long.MAX_VALUE;

		private BufferedImage spriteSet;
		private String name;
		private AnimationListener animationListener;
		private long frameDuration = STATIC_ANIMATION;
		private int nFrames = 1;
		private int row = 0;
		private Dimension spriteDimension = new Dimension();

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

		public Builder spriteDimension(final Dimension dimension) {
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

			return animation;
		}

		private List<BufferedImage> generateSprites(){
			final int yPosition = row * spriteDimension.getHeight();

			int xPosition = 0;
			final List<BufferedImage> sprites = new ArrayList<>();
			for(int i = 0; i < nFrames; i++){
				sprites.add(spriteSet.getSubimage(xPosition, yPosition, spriteDimension.getWidth(), spriteDimension.getHeight()));
				xPosition += spriteDimension.getWidth();
			}

			return sprites;
		}
	}
}