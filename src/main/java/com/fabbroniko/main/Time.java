package com.fabbroniko.main;

import lombok.SneakyThrows;

public class Time {

    private static double deltaTime, cumulativeDeltas;
    private static long variableYieldTime, lastTime;
    private static int fps, frameCount;

    public static double deltaTime() {
        return deltaTime;
    }

    public static int getFps() {
        return fps;
    }

    @SneakyThrows
    public static void sync(final int desiredFps) {
        if (desiredFps <= 0) return;

        long sleepTime = 1000000000 / desiredFps; // nanoseconds to sleep this frame

        long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
        long overSleep = 0;

        while (true) {
            long t = System.nanoTime() - lastTime;

            if (t < sleepTime - yieldTime) {
                Thread.sleep(1);
            }else if (t < sleepTime) {
                Thread.yield();
            }else {
                overSleep = t - sleepTime;
                break;
            }
        }

        final long nanoTime = System.nanoTime();
        deltaTime = ((double)(nanoTime - lastTime))/1000000000;
        lastTime = nanoTime - Math.min(overSleep, sleepTime);

        cumulativeDeltas += deltaTime;
        frameCount++;

        if(cumulativeDeltas > 1) {
            fps = (int)Math.round(frameCount / cumulativeDeltas);
            cumulativeDeltas = 0;
            frameCount = 0;
        }

        if (overSleep > variableYieldTime) {
            variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
        } else if (overSleep < variableYieldTime - 200*1000) {
            variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
        }
    }
}
