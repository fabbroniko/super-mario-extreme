package com.fabbroniko;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * Stores the user preferences set up in the Settings menu scene.
 *
 * These values are not persisted locally therefore each time the game is closed those changes are lost and restored to
 * default values.
 */
@Data
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
     * Inverts the value of the effectsActive field.
     *
     * If effects were enabled, disable them.
     * If effects were disabled, enable them.
     */
    public void invertEffectActive() {
        this.effectsAudioActive ^= true;
    }

    public void invertShowFps() { this.showFps ^= true; }
}
