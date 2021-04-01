package fabbroniko;

import java.awt.event.KeyEvent;

public class Settings {

    public static final Settings GLOBAL_SETTINGS = new Settings();

    private boolean musicActive;
    private boolean effectsAudioActive;
    private int rightMovementKeyCode;
    private int leftMovementKeyCode;
    private int jumpKeyCode;

    private Settings() {
        this.musicActive = true;
        this.effectsAudioActive = true;
        this.rightMovementKeyCode = KeyEvent.VK_RIGHT;
        this.leftMovementKeyCode = KeyEvent.VK_LEFT;
        this.jumpKeyCode = KeyEvent.VK_SPACE;
    }

    public boolean isMusicActive() {
        return musicActive;
    }

    public void setMusicActive(final boolean musicActive) {
        this.musicActive = musicActive;
    }

    public boolean isEffectsAudioActive() {
        return effectsAudioActive;
    }

    public void setEffectsAudioActive(final boolean effectsAudioActive) {
        this.effectsAudioActive = effectsAudioActive;
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
