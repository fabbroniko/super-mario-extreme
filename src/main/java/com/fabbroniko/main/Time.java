package com.fabbroniko.main;

public class Time {

    private static double deltaTime;
    private static long lastTime = System.currentTimeMillis();

    public static double deltaTime() {
        return deltaTime;
    }

    public static void tick() {
        final long currentTimeMillis = System.currentTimeMillis();
        deltaTime = ((double)(currentTimeMillis - lastTime))/1000;

        lastTime = currentTimeMillis;
    }
}
