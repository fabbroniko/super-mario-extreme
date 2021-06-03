package com.fabbroniko;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * Stores the user preferences set up in the Settings menu scene.
 *
 * These changes are saved locally in JSON format in ~/super-mario-extreme/settings.json so they can be loaded the next time the game is opened.
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

    public void invertMusicActive() {
        this.musicActive ^= true;
    }

    public void invertEffectActive() {
        this.effectsAudioActive ^= true;
    }

    public void invertShowFps() { this.showFps ^= true; }
}
