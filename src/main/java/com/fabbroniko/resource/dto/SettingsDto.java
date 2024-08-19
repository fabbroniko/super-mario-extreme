package com.fabbroniko.resource.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.event.KeyEvent;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class SettingsDto {

    private static final int DEFAULT_FPS_CAP = 60;

    private boolean musicActive = true;
    private boolean effectsAudioActive = true;
    private boolean showFps = false;
    private int rightMovementKeyCode = KeyEvent.VK_RIGHT;
    private int leftMovementKeyCode = KeyEvent.VK_LEFT;
    private int jumpKeyCode = KeyEvent.VK_SPACE;
    private int fpsCap = DEFAULT_FPS_CAP;

    public void invertMusicActive() {
        this.musicActive ^= true;
    }

    public void invertEffectActive() {
        this.effectsAudioActive ^= true;
    }

    public void invertShowFps() { this.showFps ^= true; }
}
