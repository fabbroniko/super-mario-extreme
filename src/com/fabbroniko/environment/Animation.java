package com.fabbroniko.environment;

import java.awt.image.BufferedImage;
import java.util.List;

/**
* Represents an animation for an AbstractGameObject.
* @author com.fabbroniko
*/
public final class Animation {

	private Animations animation;
	private List<BufferedImage> frames;
	private long maxTimes;
	private long currentTimes;
	private int currentFrame;
	private boolean repeatOnce;
	private AnimationListener animationListener;
	
	private static final int START_INDEX = 0;
	
	public Animation(final Animations myAnimationP) {
		this.animation = myAnimationP;
		this.currentTimes = START_INDEX;
		this.currentFrame = START_INDEX ;
		this.frames = myAnimationP.getSprites();
		this.maxTimes = myAnimationP.getFrameRepetitions();
		this.repeatOnce = myAnimationP.mustBeRepeated();
	}
	
	private void checkIndex() {
		if(currentFrame >= frames.size()) {
			currentFrame = START_INDEX ;
			if (repeatOnce && animationListener != null) {
				animationListener.animationFinished();
			}
		}
	}
	
	public void setAnimationListener(final AnimationListener animationListener) {
		this.animationListener = animationListener;
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
	
	public Animations getAnimation() {
		return this.animation;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(o == null || !(o instanceof Animation)){
			return false;
		}
		final Animation anim = (Animation)o;
		if(!anim.frames.equals(this.frames) || anim.maxTimes != this.maxTimes || anim.currentTimes != this.currentTimes || anim.currentFrame != this.currentFrame || anim.repeatOnce != this.repeatOnce){
			return false;
		}
		return true;
	}

	public interface AnimationListener {
		void animationFinished();
	}
}