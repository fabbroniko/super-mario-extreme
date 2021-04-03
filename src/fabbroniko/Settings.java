package fabbroniko;

import java.awt.event.KeyEvent;

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

    public boolean isMusicActive() {
        return musicActive;
    }

    public void invertMusicActive() {
        this.musicActive ^= true;
    }

    public boolean isEffectsAudioActive() {
        return effectsAudioActive;
    }

    public void invertEffectActive() {
        this.effectsAudioActive ^= true;
    }

    public int getRightMovementKeyCode() {
        return rightMovementKeyCode;
    }

    public void setRightMovementKeyCode(final int rightMovementKeyCode) {
        this.rightMovementKeyCode = rightMovementKeyCode;
    }

    public int getLeftMovementKeyCode() {
        return leftMovementKeyCode;
    }

    public void setLeftMovementKeyCode(final int leftMovementKeyCode) {
        this.leftMovementKeyCode = leftMovementKeyCode;
    }

    public int getJumpKeyCode() {
        return jumpKeyCode;
    }

    public void setJumpKeyCode(final int jumpKeyCode) {
        this.jumpKeyCode = jumpKeyCode;
    }
}
