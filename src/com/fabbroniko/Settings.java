package com.fabbroniko;

import java.awt.event.KeyEvent;

/**
 * Stores the user preferences set up in the Settings menu scene.
 *
 * These values are not persisted locally therefore each time the game is closed those changes are lost and restored to
 * default values.
 */
public class Settings {

    private boolean musicActive;
    private boolean effectsAudioActive;
    private int rightMovementKeyCode;
    private int leftMovementKeyCode;
    private int jumpKeyCode;

    public Settings() {
        this.musicActive = true;
        this.effectsAudioActive = true;
        this.rightMovementKeyCode = KeyEvent.VK_RIGHT;
        this.leftMovementKeyCode = KeyEvent.VK_LEFT;
        this.jumpKeyCode = KeyEvent.VK_SPACE;
    }

    /**
     * This setting allows the user to enable or disable background music in the game, win and lost scene.
     * @return true if the game is allowed to play the background music, false otherwise.
     */
    public boolean isMusicActive() {
        return musicActive;
    }

    /**
     * Inverts the value of the musicActive field.
     *
     * If music was enabled, disable it.
     * If music was disabled, enable it.
     */
    public void invertMusicActive() {
        this.musicActive ^= true;
    }

    /**
     * This setting allows the user to enable or disable sound effects in the game (like jumping, breaking blocks, etc.).
     * @return true if sound effects are allowed to be played, false otherwise.
     */
    public boolean isEffectsAudioActive() {
        return effectsAudioActive;
    }

    /**
     * Inverts the value of the effectsActive field.
     *
     * If effects were enabled, disable them.
     * If effects were disabled, enable them.
     */
    public void invertEffectActive() {
        this.effectsAudioActive ^= true;
    }

    /**
     * Returns what key code is associated to move to the right.
     * @return The key code of the key the user should press to move to the right.
     */
    public int getRightMovementKeyCode() {
        return rightMovementKeyCode;
    }

    /**
     * Sets the key code associated to moving Mario to the right
     *
     * @see KeyEvent#getKeyCode()
     * @param rightMovementKeyCode The key code of the key to use to move to the right
     */
    public void setRightMovementKeyCode(final int rightMovementKeyCode) {
        this.rightMovementKeyCode = rightMovementKeyCode;
    }

    /**
     * Returns what key code is associated to move to the left.
     * @return The key code of the key the user should press to move to the left.
     */
    public int getLeftMovementKeyCode() {
        return leftMovementKeyCode;
    }

    /**
     * Sets the key code associated to moving Mario to the left
     *
     * @see KeyEvent#getKeyCode()
     * @param leftMovementKeyCode The key code of the key to use to move to the left
     */
    public void setLeftMovementKeyCode(final int leftMovementKeyCode) {
        this.leftMovementKeyCode = leftMovementKeyCode;
    }

    /**
     * Returns what key code is associated to jumping.
     * @return The key code of the key the user should press to jump.
     */
    public int getJumpKeyCode() {
        return jumpKeyCode;
    }

    /**
     * Sets the key code associated to jumping
     *
     * @see KeyEvent#getKeyCode()
     * @param jumpKeyCode The key code of the key to use to jump
     */
    public void setJumpKeyCode(final int jumpKeyCode) {
        this.jumpKeyCode = jumpKeyCode;
    }
}
