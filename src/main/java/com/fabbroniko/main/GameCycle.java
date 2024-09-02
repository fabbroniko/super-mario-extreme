package com.fabbroniko.main;

public interface GameCycle {

    void run(final CycleListener cycleListener);

    void stop();
}
