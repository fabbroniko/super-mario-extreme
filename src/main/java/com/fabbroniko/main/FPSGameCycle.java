package com.fabbroniko.main;

import com.fabbroniko.settings.SettingsProvider;
import lombok.SneakyThrows;
import org.example.annotation.Component;

@Component
public class FPSGameCycle implements GameCycle {

    private final SettingsProvider settingsProvider;

    private boolean running = false;

    public FPSGameCycle(final SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    @SneakyThrows
    @Override
    public void run(final CycleListener cycleListener) {
        running = true;

        cycleListener.init();

        while (running) {
            cycleListener.update();
            cycleListener.draw();
            Time.sync(settingsProvider.getSettings().getFpsCap());
        }

        cycleListener.close();
    }

    @Override
    public void stop() {
        running = false;
    }
}
