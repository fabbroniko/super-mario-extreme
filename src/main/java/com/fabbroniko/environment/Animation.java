package com.fabbroniko.environment;

import java.awt.image.BufferedImage;
import java.util.List;

/**
* Represents an animation for an AbstractGameObject.
* @author com.fabbroniko
*/
public final class Animation {

	private static final int START_INDEX = 0;

	private final List<BufferedImage> frames;
	private final long maxTimes;
	private final boolean repeatOnce;
	private final AnimationListener animationListener;
	private final String name;

	private long currentTimes;
	private int currentFrame;

	public Animation(final String name, final List<BufferedImage> frames, final int frameRepetition, final boolean repeat, final AnimationListener animationListener) {
		this.name = name;
		this.frames = frames;
		this.currentTimes = START_INDEX;
		this.currentFrame = START_INDEX;
		this.maxTimes = frameRepetition;
		this.repeatOnce = repeat;
		this.animationListener = animationListener;
	}
	
	private void checkIndex() {
		if(currentFrame >= frames.size()) {
			currentFrame = START_INDEX ;
			if (repeatOnce && animationListener != null) {
				animationListener.animationFinished();
			}
		}
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Get's the current frame.
	 * @return The current frame.
	 */
	public BufferedImage getImage() {
		final BufferedImage tmp = this.frames.get(currentFrame);
		currentTimes++;
		if (currentTimes >= maxTimes) {
			currentTimes = 0;
			currentFrame++;
			checkIndex();
		}
		return tmp;
	}
	
	/**
	 * Resets this animation.
	 */
	public void reset() {
		currentFrame = 0;
		currentTimes = 0;
	}

	@Override
	public boolean equals(final Object o) {
		if(!(o instanceof Animation)){
			return false;
		}

		final Animation anim = (Animation)o;
		return anim.frames.equals(this.frames) && anim.maxTimes == this.maxTimes && anim.currentTimes == this.currentTimes && anim.currentFrame == this.currentFrame && anim.repeatOnce == this.repeatOnce;
	}

	public interface AnimationListener {
		void animationFinished();
	}
}