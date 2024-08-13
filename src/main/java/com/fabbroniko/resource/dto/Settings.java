package com.fabbroniko.resource.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.awt.event.KeyEvent;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class Settings {

    private static final int DEFAULT_FPS_CAP = 60;

    private boolean musicActive;
    private boolean effectsAudioActive;
    private boolean showFps;
    private int rightMovementKeyCode;
    private int leftMovementKeyCode;
    private int jumpKeyCode;
    private int fpsCap;

    public Settings() {
        this.musicActive = true;
        this.effectsAudioActive = true;
        this.showFps = false;
        this.rightMovementKeyCode = KeyEvent.VK_RIGHT;
        this.leftMovementKeyCode = KeyEvent.VK_LEFT;
        this.jumpKeyCode = KeyEvent.VK_SPACE;
        this.fpsCap = DEFAULT_FPS_CAP;
    }

    public void invertMusicActive() {
        this.musicActive ^= true;
    }

    public void invertEffectActive() {
        this.effectsAudioActive ^= true;
    }

    public void invertShowFps() { this.showFps ^= true; }

    public boolean isMusicActive() {
        return musicActive;
    }

    public boolean isEffectsAudioActive() {
        return effectsAudioActive;
    }

    public boolean isShowFps() {
        return showFps;
    }

    public int getRightMovementKeyCode() {
        return rightMovementKeyCode;
    }

    public int getLeftMovementKeyCode() {
        return leftMovementKeyCode;
    }

    public int getJumpKeyCode() {
        return jumpKeyCode;
    }

    public int getFpsCap() {
        return fpsCap;
    }

    public void setMusicActive(boolean musicActive) {
        this.musicActive = musicActive;
    }

    public void setEffectsAudioActive(boolean effectsAudioActive) {
        this.effectsAudioActive = effectsAudioActive;
    }

    public void setShowFps(boolean showFps) {
        this.showFps = showFps;
    }

    public void setRightMovementKeyCode(int rightMovementKeyCode) {
        this.rightMovementKeyCode = rightMovementKeyCode;
    }

    public void setLeftMovementKeyCode(int leftMovementKeyCode) {
        this.leftMovementKeyCode = leftMovementKeyCode;
    }

    public void setJumpKeyCode(int jumpKeyCode) {
        this.jumpKeyCode = jumpKeyCode;
    }

    public void setFpsCap(int fpsCap) {
        this.fpsCap = fpsCap;
    }
}
